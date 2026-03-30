package sollecitom.libs.swissknife.security.scan

import org.json.JSONObject
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.BindMode
import org.testcontainers.images.builder.Transferable
import java.time.Duration

/** Scans Docker images for vulnerabilities using Trivy running as a container via Testcontainers. */
object TrivyImageScanner {

    private const val TRIVY_IMAGE = "aquasec/trivy:latest"

    /**
     * Scans the given Docker [imageName] for vulnerabilities at or above the specified [severities].
     * Optionally reads a [trivyIgnoreContent] string (one CVE ID per line) to suppress known issues.
     *
     * @return list of vulnerabilities found, filtered by severity and exclusions
     */
    fun scan(
        imageName: String,
        severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
        trivyIgnoreContent: String? = null
    ): List<Vulnerability> {

        val severityArg = severities.joinToString(",") { it.name }
        val command = mutableListOf(
            "trivy", "image",
            "--format", "json",
            "--severity", severityArg,
            "--no-progress",
            "--skip-db-update=false"
        )
        if (trivyIgnoreContent != null) {
            command.addAll(listOf("--ignorefile", "/tmp/.trivyignore"))
        }
        command.add(imageName)

        val container = GenericContainer(TRIVY_IMAGE)
            .withCommand(*command.toTypedArray())
            .withFileSystemBind("/var/run/docker.sock", "/var/run/docker.sock", BindMode.READ_ONLY)
            .withStartupTimeout(Duration.ofMinutes(5))
            .withCreateContainerCmdModifier { cmd ->
                cmd.withEntrypoint("/bin/sh", "-c")
            }

        // We need to run trivy as a one-shot command and capture output
        // Using docker run directly since GenericContainer is for long-running services
        return runTrivyScan(imageName, severityArg, trivyIgnoreContent)
    }

    private fun runTrivyScan(imageName: String, severities: String, trivyIgnoreContent: String?): List<Vulnerability> {

        val ignoreArgs = if (trivyIgnoreContent != null) {
            val tempFile = kotlin.io.path.createTempFile(prefix = "trivyignore").toFile()
            tempFile.writeText(trivyIgnoreContent)
            tempFile.deleteOnExit()
            listOf("--ignorefile", tempFile.absolutePath)
        } else emptyList()

        val command = mutableListOf(
            "docker", "run", "--rm",
            "-v", "/var/run/docker.sock:/var/run/docker.sock"
        )
        if (ignoreArgs.isNotEmpty()) {
            command.addAll(listOf("-v", "${ignoreArgs[1]}:/tmp/.trivyignore:ro"))
        }
        command.addAll(listOf(
            TRIVY_IMAGE,
            "image",
            "--format", "json",
            "--severity", severities,
            "--no-progress",
        ))
        if (ignoreArgs.isNotEmpty()) {
            command.addAll(listOf("--ignorefile", "/tmp/.trivyignore"))
        }
        command.add(imageName)

        val process = ProcessBuilder(command)
            .redirectErrorStream(false)
            .start()

        val output = process.inputStream.bufferedReader().readText()
        val errors = process.errorStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        if (output.isBlank()) {
            if (errors.isNotBlank()) {
                throw IllegalStateException("Trivy scan failed (exit code $exitCode): $errors")
            }
            return emptyList()
        }

        return parseVulnerabilities(output)
    }

    private fun parseVulnerabilities(jsonOutput: String): List<Vulnerability> {

        val json = org.json.JSONObject("{\"wrapper\": $jsonOutput}")
        val results = json.optJSONArray("wrapper") ?: return emptyList()

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
