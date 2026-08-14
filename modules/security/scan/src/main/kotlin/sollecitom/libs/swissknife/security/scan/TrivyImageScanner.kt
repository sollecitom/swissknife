package sollecitom.libs.swissknife.security.scan

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.OutputFrame
import org.testcontainers.containers.output.ToStringConsumer
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy
import org.testcontainers.images.builder.Transferable
import java.io.File
import java.time.Duration

/** Raised when Trivy could not complete a scan, as opposed to completing one that found vulnerabilities. */
class TrivyScanFailed(imageName: String, trivyImage: String, attempts: Int, val output: String, cause: Throwable?) :
    IllegalStateException("$trivyImage failed to scan '$imageName' after $attempts attempt(s). Trivy output:\n${output.ifBlank { "<no output captured>" }}", cause)

/** Scans Docker images for vulnerabilities using Trivy running as a container via Testcontainers. */
object TrivyImageScanner {

    private const val DEFAULT_TRIVY_IMAGE = "aquasec/trivy"
    private const val DEFAULT_TRIVY_VERSION = "0.74.0"

    /** Trivy's cache directory inside the container: the vulnerability DB and the Java DB both live here. */
    private const val CONTAINER_CACHE_DIRECTORY = "/root/.cache/trivy"

    private const val DEFAULT_MAXIMUM_ATTEMPTS = 3
    private val DELAY_BETWEEN_ATTEMPTS: Duration = Duration.ofSeconds(5)

    /**
     * Scans the given Docker [imageName] for vulnerabilities at or above the specified [severities].
     * Trivy runs as a Docker container (auto-pulled by Testcontainers) with access to the Docker socket.
     *
     * Trivy downloads its vulnerability DB and Java DB on every run unless they are cached, and those downloads
     * dominate the runtime and fail intermittently (typically `unexpected EOF` part-way through the Java DB).
     * [cacheDirectory] is therefore mounted as Trivy's cache so the DBs are fetched once and reused, and a scan
     * that fails anyway is retried up to [maximumAttempts] times.
     *
     * @param imageName the Docker image to scan (must be available locally)
     * @param severities which severity levels to report (default: CRITICAL, HIGH)
     * @param trivyIgnoreContent optional .trivyignore content (one CVE ID per line) for suppressing known issues
     * @param trivyVersion Trivy image version to use (default: [DEFAULT_TRIVY_VERSION])
     * @param cacheDirectory host directory holding Trivy's DB cache across runs
     * @param maximumAttempts how many times to run Trivy before giving up
     * @return list of vulnerabilities found
     * @throws TrivyScanFailed when Trivy could not complete the scan
     */
    fun scan(
        imageName: String,
        severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
        trivyIgnoreContent: String? = null,
        trivyVersion: String = System.getProperty("securityScan.trivyVersion") ?: DEFAULT_TRIVY_VERSION,
        cacheDirectory: File = defaultCacheDirectory(),
        maximumAttempts: Int = System.getProperty("securityScan.maximumAttempts")?.toIntOrNull() ?: DEFAULT_MAXIMUM_ATTEMPTS
    ): List<Vulnerability> {

        require(maximumAttempts >= 1) { "maximumAttempts must be >= 1 but was $maximumAttempts" }
        cacheDirectory.mkdirs()

        val severityArg = severities.joinToString(",") { it.name }
        val command = buildList {
            add("image")
            add("--format"); add("json")
            add("--severity"); add(severityArg)
            add("--no-progress")
            if (trivyIgnoreContent != null) {
                add("--ignorefile"); add("/tmp/.trivyignore")
            }
            add(imageName)
        }

        val trivyImage = "$DEFAULT_TRIVY_IMAGE:$trivyVersion"
        var lastFailure: Throwable? = null
        var lastOutput = ""

        repeat(maximumAttempts) { attemptIndex ->
            val outputConsumer = ToStringConsumer()
            val container = newContainer(trivyImage, command, cacheDirectory, trivyIgnoreContent, outputConsumer)
            try {
                container.start()
                return parseReport(reportOutput(container, outputConsumer), imageName, trivyImage)
            } catch (failure: Exception) {
                lastFailure = failure
                lastOutput = capturedOutput(container, outputConsumer)
                val attemptsLeft = maximumAttempts - attemptIndex - 1
                // A missing image is a real error rather than a flaky download, so there is nothing to gain by retrying.
                if (attemptsLeft == 0 || !isWorthRetrying(lastOutput)) return@repeat
                println("Trivy scan of '$imageName' failed (${failureSummary(lastOutput)}); retrying, $attemptsLeft attempt(s) left.")
                System.out.flush()
                Thread.sleep(DELAY_BETWEEN_ATTEMPTS.toMillis())
            } finally {
                runCatching { container.stop() }
            }
        }
        throw TrivyScanFailed(imageName, trivyImage, maximumAttempts, lastOutput, lastFailure)
    }

    /**
     * Defaults to Trivy's own conventional cache location, so a Trivy installed on the host shares the DBs with
     * the containerised one instead of each maintaining a separate multi-hundred-megabyte copy.
     */
    internal fun defaultCacheDirectory(): File = System.getProperty("securityScan.cacheDirectory")?.let(::File)
        ?: File(System.getProperty("user.home"), ".cache/trivy")

    private fun newContainer(trivyImage: String, command: List<String>, cacheDirectory: File, trivyIgnoreContent: String?, outputConsumer: ToStringConsumer) = GenericContainer(trivyImage).apply {
        withCommand(*command.toTypedArray())
        withFileSystemBind("/var/run/docker.sock", "/var/run/docker.sock", BindMode.READ_ONLY)
        withFileSystemBind(cacheDirectory.absolutePath, CONTAINER_CACHE_DIRECTORY, BindMode.READ_WRITE)
        withStartupCheckStrategy(OneShotStartupCheckStrategy().withTimeout(Duration.ofMinutes(5)))
        withLogConsumer(outputConsumer)
        if (trivyIgnoreContent != null) {
            withCopyToContainer(Transferable.of(trivyIgnoreContent.toByteArray()), "/tmp/.trivyignore")
        }
    }

    /**
     * Reads the report from the exited container's STDOUT.
     *
     * Fetched synchronously rather than taken from the streamed log consumer, which can still be mid-delivery
     * when the container exits and would then yield truncated JSON. STDOUT alone also keeps Trivy's progress
     * logging, which goes to STDERR, from landing in the middle of the JSON.
     */
    private fun reportOutput(container: GenericContainer<*>, outputConsumer: ToStringConsumer): String {
        val standardOutput = runCatching { container.getLogs(OutputFrame.OutputType.STDOUT) }.getOrDefault("")
        return standardOutput.ifBlank { runCatching { outputConsumer.toUtf8String() }.getOrDefault("") }
    }

    /** The diagnostic counterpart of [reportOutput]: everything the container emitted, for failure messages. */
    private fun capturedOutput(container: GenericContainer<*>, outputConsumer: ToStringConsumer): String {
        val streamed = runCatching { outputConsumer.toUtf8String() }.getOrDefault("")
        return streamed.ifBlank { runCatching { container.logs }.getOrDefault("") }
    }

    /**
     * Turns Trivy's report into findings, failing when the report cannot be read at all.
     *
     * A scanner that cannot parse its own output must not report an image as clean: that is a silent false pass
     * on a security gate. Only a report that genuinely lists no vulnerabilities yields an empty result.
     */
    internal fun parseReport(output: String, imageName: String, trivyImage: String): List<Vulnerability> {
        extractJson(output) ?: throw TrivyScanFailed(imageName, trivyImage, attempts = 1, output = output, cause = null)
        return parseVulnerabilities(output)
    }

    internal fun isWorthRetrying(output: String) = !output.contains("unable to find the specified image")

    private fun failureSummary(output: String) = output.lineSequence().lastOrNull { it.contains("FATAL") }?.trim()?.take(200) ?: "no Trivy output captured"

    internal fun parseVulnerabilities(output: String): List<Vulnerability> {

        // Testcontainers captures stdout and stderr together. Trivy outputs JSON to stdout
        // and log/progress messages to stderr. We need to extract just the JSON object.
        // Trivy's JSON format wraps results in {"Results": [...]}, so find the last top-level JSON object.
        val jsonText = extractJson(output) ?: return emptyList()

        val results = try {
            org.json.JSONObject(jsonText).optJSONArray("Results") ?: return emptyList()
        } catch (_: Exception) {
            return emptyList()
        }

        val vulnerabilities = mutableListOf<Vulnerability>()
        for (i in 0 until results.length()) {
            val result = results.optJSONObject(i) ?: continue
            val target = result.optString("Target", "unknown")
            val targetClass = result.optString("Class", "unknown")
            val vulns = result.optJSONArray("Vulnerabilities") ?: continue
            for (j in 0 until vulns.length()) {
                val vuln = vulns.optJSONObject(j) ?: continue
                vulnerabilities.add(
                    Vulnerability(
                        id = vuln.getString("VulnerabilityID"),
                        packageName = vuln.getString("PkgName"),
                        installedVersion = vuln.getString("InstalledVersion"),
                        fixedVersion = vuln.optString("FixedVersion", null),
                        severity = runCatching { Severity.valueOf(vuln.getString("Severity")) }.getOrDefault(Severity.UNKNOWN),
                        title = vuln.optString("Title", "No description"),
                        target = target,
                        targetClass = targetClass
                    )
                )
            }
        }
        return vulnerabilities
    }

    private fun extractJson(output: String): String? {
        // Find the JSON object by looking for balanced braces starting from the first {
        // that parses and looks like a Trivy report. "Results" is absent from a report with nothing to report,
        // so "SchemaVersion" also identifies one — otherwise a clean scan would look like unreadable output.
        var index = 0
        while (index < output.length) {
            val braceStart = output.indexOf('{', index)
            if (braceStart == -1) return null
            val candidate = output.substring(braceStart)
            try {
                val json = org.json.JSONObject(candidate)
                if (json.has("Results") || json.has("SchemaVersion")) return candidate
            } catch (_: Exception) {
                // Not valid JSON starting here, try next {
            }
            index = braceStart + 1
        }
        return null
    }
}
