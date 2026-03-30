package sollecitom.libs.swissknife.security.scan

import assertk.Assert
import assertk.assertThat

/** Creates an assertk assertion on a [DockerImage] for security scanning. */
fun assertThatImage(imageName: String): Assert<DockerImage> = assertThat(DockerImage(imageName))

/** Wrapper around a Docker image name for use with assertk assertions. */
data class DockerImage(val name: String)

/**
 * Asserts that the Docker image has no unacceptable vulnerabilities.
 * Unacceptable means: at the given [severities] (default CRITICAL + HIGH), and not suppressed by [trivyIgnoreContent].
 *
 * @param severities which severity levels are unacceptable (default: CRITICAL, HIGH)
 * @param trivyIgnoreContent optional .trivyignore content (one CVE ID per line) for accepted vulnerabilities
 */
fun Assert<DockerImage>.hasNoUnacceptableVulnerabilities(
    severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
    trivyIgnoreContent: String? = null
) = given { image ->
    val vulnerabilities = TrivyImageScanner.scan(image.name, severities, trivyIgnoreContent)
    if (vulnerabilities.isNotEmpty()) {
        throw AssertionError(formatReport(image.name, vulnerabilities))
    }
}

private fun formatReport(imageName: String, vulnerabilities: List<Vulnerability>): String {

    val sorted = vulnerabilities.sortedWith(compareBy<Vulnerability> { it.severity }.thenBy { it.id })
    val grouped = sorted.groupBy { it.target }

    val criticalCount = vulnerabilities.count { it.severity == Severity.CRITICAL }
    val highCount = vulnerabilities.count { it.severity == Severity.HIGH }
    val countsDescription = buildList {
        if (criticalCount > 0) add("$criticalCount critical")
        if (highCount > 0) add("$highCount high")
    }.joinToString(", ")

    return buildString {
        appendLine()
        appendLine("╔══════════════════════════════════════════════════════════════════════════════")
        appendLine("║ SECURITY SCAN FAILED — $imageName")
        appendLine("║ Found ${vulnerabilities.size} unacceptable vulnerabilities ($countsDescription)")
        appendLine("╠══════════════════════════════════════════════════════════════════════════════")
        appendLine("║")
        appendLine("║ VULNERABILITIES")
        appendLine("║")

        for ((target, vulns) in grouped) {
            appendLine("║   $target")
            appendLine("║")
            for (vuln in vulns) {
                val severityTag = when (vuln.severity) {
                    Severity.CRITICAL -> "🔴 CRITICAL"
                    Severity.HIGH -> "🟠 HIGH"
                    Severity.MEDIUM -> "🟡 MEDIUM"
                    Severity.LOW -> "🟢 LOW"
                    Severity.UNKNOWN -> "⚪ UNKNOWN"
                }
                appendLine("║     $severityTag  ${vuln.id}")
                appendLine("║       ${vuln.packageName}@${vuln.installedVersion}")
                appendLine("║       ${vuln.title}")
                appendLine("║")
            }
        }

        appendLine("╠══════════════════════════════════════════════════════════════════════════════")
        appendLine("║")
        appendLine("║ REMEDIATION PLAN")
        appendLine("║")

        val osVulns = sorted.filter { it.targetClass == "os-pkgs" }
        val langVulns = sorted.filter { it.targetClass == "lang-pkgs" }
        val otherVulns = sorted.filter { it.targetClass != "os-pkgs" && it.targetClass != "lang-pkgs" }

        if (osVulns.isNotEmpty()) {
            appendLine("║   Update base Docker image to pick up OS package fixes:")
            for (vuln in osVulns) {
                val fix = vuln.fixedVersion?.let { "→ $it" } ?: "(no fix available — consider suppressing)"
                appendLine("║     • ${vuln.packageName}@${vuln.installedVersion} $fix")
            }
            appendLine("║")
        }

        if (langVulns.isNotEmpty()) {
            appendLine("║   Update dependency versions in libs.versions.toml:")
            for (vuln in langVulns) {
                val fix = vuln.fixedVersion?.let { "→ $it" } ?: "(no fix available — consider suppressing)"
                appendLine("║     • ${vuln.packageName}@${vuln.installedVersion} $fix")
            }
            appendLine("║")
        }

        if (otherVulns.isNotEmpty()) {
            appendLine("║   Other:")
            for (vuln in otherVulns) {
                val fix = vuln.fixedVersion?.let { "→ $it" } ?: "(no fix available)"
                appendLine("║     • ${vuln.packageName}@${vuln.installedVersion} $fix")
            }
            appendLine("║")
        }

        appendLine("║   To suppress accepted vulnerabilities, add CVE IDs to .trivyignore")
        appendLine("║")
        appendLine("╚══════════════════════════════════════════════════════════════════════════════")
    }
}
