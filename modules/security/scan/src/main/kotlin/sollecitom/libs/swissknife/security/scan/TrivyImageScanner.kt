package sollecitom.libs.swissknife.security.scan

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.ToStringConsumer
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy
import org.testcontainers.images.builder.Transferable
import java.time.Duration

/** Scans Docker images for vulnerabilities using Trivy running as a container via Testcontainers. */
object TrivyImageScanner {

    private const val DEFAULT_TRIVY_IMAGE = "aquasec/trivy"
    private const val DEFAULT_TRIVY_VERSION = "0.70.0"

    /**
     * Scans the given Docker [imageName] for vulnerabilities at or above the specified [severities].
     * Trivy runs as a Docker container (auto-pulled by Testcontainers) with access to the Docker socket.
     *
     * @param imageName the Docker image to scan (must be available locally)
     * @param severities which severity levels to report (default: CRITICAL, HIGH)
     * @param trivyIgnoreContent optional .trivyignore content (one CVE ID per line) for suppressing known issues
     * @param trivyVersion Trivy image version to use (default: [DEFAULT_TRIVY_VERSION])
     * @return list of vulnerabilities found
     */
    fun scan(
        imageName: String,
        severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
        trivyIgnoreContent: String? = null,
        trivyVersion: String = System.getProperty("securityScan.trivyVersion") ?: DEFAULT_TRIVY_VERSION
    ): List<Vulnerability> {

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

        val outputConsumer = ToStringConsumer()

        val trivyImage = "$DEFAULT_TRIVY_IMAGE:$trivyVersion"
        val container = GenericContainer(trivyImage).apply {
            withCommand(*command.toTypedArray())
            withFileSystemBind("/var/run/docker.sock", "/var/run/docker.sock", BindMode.READ_ONLY)
            withStartupCheckStrategy(OneShotStartupCheckStrategy().withTimeout(Duration.ofMinutes(5)))
            withLogConsumer(outputConsumer)
            if (trivyIgnoreContent != null) {
                withCopyToContainer(Transferable.of(trivyIgnoreContent.toByteArray()), "/tmp/.trivyignore")
            }
        }

        container.start()

        val output = outputConsumer.toUtf8String()
        return parseVulnerabilities(output)
    }

    private fun parseVulnerabilities(output: String): List<Vulnerability> {

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
        // that successfully parses as a JSONObject with a "Results" key
        var index = 0
        while (index < output.length) {
            val braceStart = output.indexOf('{', index)
            if (braceStart == -1) return null
            val candidate = output.substring(braceStart)
            try {
                val json = org.json.JSONObject(candidate)
                if (json.has("Results")) return candidate
            } catch (_: Exception) {
                // Not valid JSON starting here, try next {
            }
            index = braceStart + 1
        }
        return null
    }
}
