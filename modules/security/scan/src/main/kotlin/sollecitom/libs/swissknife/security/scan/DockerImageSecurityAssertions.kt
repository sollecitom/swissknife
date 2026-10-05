package sollecitom.libs.swissknife.security.scan

import assertk.Assert
import assertk.assertThat
import java.io.File

/** Creates an assertk assertion on a [DockerImage] for security scanning. */
fun assertThatImage(imageName: String): Assert<DockerImage> = assertThat(DockerImage(imageName))

/** Wrapper around a Docker image name for use with assertk assertions. */
data class DockerImage(val name: String)

/**
 * Asserts that the Docker image has no unacceptable vulnerabilities.
 *
 * Unacceptable means: at the given [severities] (default CRITICAL + HIGH) and NOT explicitly accepted.
 *
 * Accepted CVEs are read from [acceptedVulnerabilitiesContent] (the contents of a `.trivyignore` file), or, when that
 * is `null`, from the file pointed to by the `securityScan.trivyIgnoreFile` system property (set by the Gradle
 * `securityScan` convention). Accepted CVEs do NOT fail the build — but, unlike Trivy's `--ignorefile`, they are NOT
 * silently dropped: any accepted CVE that is still present in the image is reported as a loud WARNING. This keeps
 * suppressed issues visible and the warning self-clears once the CVE is no longer present (e.g. after a base-image
 * rebuild), at which point the stale `.trivyignore` entry can be removed.
 *
 * `.trivyignore` format: one CVE ID per line; lines starting with `#` are comments; an inline `# reason` after a CVE
 * ID is shown in the warning to explain why it was accepted.
 *
 * @param severities which severity levels are unacceptable (default: CRITICAL, HIGH)
 * @param acceptedVulnerabilitiesContent optional `.trivyignore` content listing accepted (warned, non-failing) CVEs
 */
fun Assert<DockerImage>.hasNoUnacceptableVulnerabilities(
    severities: Set<Severity> = setOf(Severity.CRITICAL, Severity.HIGH),
    acceptedVulnerabilitiesContent: String? = null
) = given { image ->
    val acceptedContent = acceptedVulnerabilitiesContent ?: readAcceptedVulnerabilitiesFile()
    val accepted = acceptedContent?.let(::parseAcceptedVulnerabilities).orEmpty()

    val vulnerabilities = TrivyImageScanner.scan(image.name, severities)
    val (acceptedPresent, unacceptable) = vulnerabilities.partition { it.id in accepted }

    // A `.trivyignore` entry whose CVE is no longer present means the fix has landed (e.g. base-image rebuild):
    // flag it so the stale suppression gets removed, instead of lingering and masking a future re-occurrence.
    val staleAcceptedIds = accepted.keys - vulnerabilities.mapTo(mutableSetOf()) { it.id }
    if (staleAcceptedIds.isNotEmpty()) {
        println(formatStaleWarning(image.name, staleAcceptedIds, accepted))
        System.out.flush()
    }
    if (acceptedPresent.isNotEmpty()) {
        println(formatAcceptedWarning(image.name, acceptedPresent, accepted))
        System.out.flush()
    }
    if (unacceptable.isNotEmpty()) {
        throw AssertionError(formatReport(image.name, unacceptable))
    }
}

private fun formatStaleWarning(imageName: String, staleIds: Set<String>, reasons: Map<String, String>): String = buildString {
    appendLine()
    appendLine("╔══════════════════════════════════════════════════════════════════════════════")
    appendLine("║ ✅ SECURITY SCAN — ${staleIds.size} accepted vulnerability(ies) NO LONGER present — remove from .trivyignore")
    appendLine("║ $imageName")
    appendLine("╠══════════════════════════════════════════════════════════════════════════════")
    appendLine("║")
    for (id in staleIds.sorted()) {
        appendLine("║   $id")
        reasons[id]?.takeIf { it.isNotBlank() }?.let { appendLine("║     was accepted because: $it") }
    }
    appendLine("║")
    appendLine("║ The underlying issue appears fixed. Delete these lines from .trivyignore so a")
    appendLine("║ future re-occurrence of the same CVE is not silently suppressed.")
    appendLine("╚══════════════════════════════════════════════════════════════════════════════")
}

private fun readAcceptedVulnerabilitiesFile(): String? = System.getProperty("securityScan.trivyIgnoreFile")
    ?.let { path -> runCatching { File(path).readText() }.getOrNull() }

/** Parses `.trivyignore` content into a map of CVE ID to its (optional) acceptance reason (the inline `# ...` comment). */
private fun parseAcceptedVulnerabilities(content: String): Map<String, String> = buildMap {
    content.lineSequence().forEach { rawLine ->
        val line = rawLine.trim()
        if (line.isEmpty() || line.startsWith("#")) return@forEach
        val hashIndex = line.indexOf('#')
        val id = (if (hashIndex >= 0) line.substring(0, hashIndex) else line).trim()
        val reason = if (hashIndex >= 0) line.substring(hashIndex + 1).trim() else ""
        if (id.isNotEmpty()) put(id, reason)
    }
}

private fun severityTag(severity: Severity): String = when (severity) {
    Severity.CRITICAL -> "🔴 CRITICAL"
    Severity.HIGH -> "🟠 HIGH"
    Severity.MEDIUM -> "🟡 MEDIUM"
    Severity.LOW -> "🟢 LOW"
    Severity.UNKNOWN -> "⚪ UNKNOWN"
}

private fun formatAcceptedWarning(imageName: String, vulnerabilities: List<Vulnerability>, reasons: Map<String, String>): String {

    val sorted = vulnerabilities.sortedWith(compareBy<Vulnerability> { it.severity }.thenBy { it.id })

    return buildString {
        appendLine()
        appendLine("╔══════════════════════════════════════════════════════════════════════════════")
        appendLine("║ ⚠️  SECURITY SCAN WARNING — ${vulnerabilities.size} accepted vulnerability(ies) suppressed (build NOT failed)")
        appendLine("║ $imageName")
        appendLine("╠══════════════════════════════════════════════════════════════════════════════")
        appendLine("║")
        for (vuln in sorted) {
            appendLine("║   ${severityTag(vuln.severity)}  ${vuln.id}")
            appendLine("║     ${vuln.packageName}@${vuln.installedVersion}")
            appendLine("║     ${vuln.title}")
            reasons[vuln.id]?.takeIf { it.isNotBlank() }?.let { appendLine("║     accepted because: $it") }
            appendLine("║")
        }
        appendLine("║ These are suppressed via .trivyignore. Re-check on every base-image rebuild;")
        appendLine("║ remove the entry once the CVE is no longer reported here.")
        appendLine("╚══════════════════════════════════════════════════════════════════════════════")
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
                appendLine("║     ${severityTag(vuln.severity)}  ${vuln.id}")
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
