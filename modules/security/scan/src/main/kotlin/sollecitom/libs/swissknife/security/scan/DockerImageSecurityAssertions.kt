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
        val report = vulnerabilities
            .sortedWith(compareBy<Vulnerability> { it.severity }.thenBy { it.id })
            .joinToString("\n  ") { it.toString() }
        throw AssertionError("Expected Docker image '${image.name}' to have no unacceptable vulnerabilities, but found ${vulnerabilities.size}:\n  $report")
    }
}
