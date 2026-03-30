package sollecitom.libs.swissknife.security.scan

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.ToStringConsumer
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy
import org.testcontainers.images.builder.Transferable
import java.time.Duration

/** Scans Docker images for vulnerabilities using Trivy running as a container via Testcontainers. */
object TrivyImageScanner {

    private const val TRIVY_IMAGE = "aquasec/trivy:0.69.3"

    /**
     * Scans the given Docker [imageName] for vulnerabilities at or above the specified [severities].
     * Trivy runs as a Docker container (auto-pulled by Testcontainers) with access to the Docker socket.
     *
     * @param imageName the Docker image to scan (must be available locally)
     * @param severities which severity levels to report (default: CRITICAL, HIGH)
     * @param trivyIgnoreContent optional .trivyignore content (one CVE ID per line) for suppressing known issues
     * @return list of vulnerabilities found
     */
    fun scan(
        imageName: String,
        severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
        trivyIgnoreContent: String? = null
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

        val container = GenericContainer(TRIVY_IMAGE).apply {
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

        // Trivy outputs JSON to stdout but log messages go to stderr (captured together by Testcontainers)
        // Extract the JSON portion — find the first [ or { that starts the JSON output
        val jsonStart = output.indexOfFirst { it == '[' || it == '{' }
        if (jsonStart == -1) return emptyList()
        val jsonText = output.substring(jsonStart).trim()

        val results = try {
            if (jsonText.startsWith("[")) org.json.JSONArray(jsonText)
            else org.json.JSONObject(jsonText).optJSONArray("Results") ?: return emptyList()
        } catch (_: Exception) {
            return emptyList()
        }

        val vulnerabilities = mutableListOf<Vulnerability>()
        for (i in 0 until results.length()) {
            val result = results.getJSONObject(i)
            val vulns = result.optJSONArray("Vulnerabilities") ?: continue
            for (j in 0 until vulns.length()) {
                val vuln = vulns.getJSONObject(j)
                vulnerabilities.add(
                    Vulnerability(
                        id = vuln.getString("VulnerabilityID"),
                        packageName = vuln.getString("PkgName"),
                        installedVersion = vuln.getString("InstalledVersion"),
                        fixedVersion = vuln.optString("FixedVersion", null),
                        severity = runCatching { Severity.valueOf(vuln.getString("Severity")) }.getOrDefault(Severity.UNKNOWN),
                        title = vuln.optString("Title", "No description")
                    )
                )
            }
        }
        return vulnerabilities
    }
}
