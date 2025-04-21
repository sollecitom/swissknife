package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import io.swagger.v3.oas.models.OpenAPI


class WhitelistedAlphabetPathNameRule(val alphabet: Set<Char>, val templatedPathSegmentsAlphabet: Set<Char> = alphabet, private val versioningPathPrefixRule: MandatoryVersioningPathPrefixRule? = null, private val allowedPathSegments: Set<Regex> = emptySet()) : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.paths.asSequence().map { it.key }.map { it.removeVersioningPath() }.mapNotNull { check(it) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(path: Pair<String, String>): Violation? {
        val (pathName, pathToCheck) = path
        val segments = pathToCheck.split(pathSegmentSeparator).filterNot { it.isBlank() }
        segments.filterNot { segment -> allowedPathSegments.any { regex -> regex.matchEntire(segment)?.let { true } ?: false } }.forEach { pathSegment ->
            if (!pathSegment.isTemplatedSegment() && pathSegment.any { character -> character !in alphabet }) return violationForPath(pathName)
            if (pathSegment.isTemplatedSegment() && pathSegment.removePrefix(pathTemplateStart.toString()).removeSuffix(pathTemplateEnd.toString()).any { character -> character !in templatedPathSegmentsAlphabet }) return violationForPath(pathName)
        }
        return null
    }

    private fun String.removeVersioningPath(): Pair<String, String> {

        if (versioningPathPrefixRule == null) return this to this
        val versioningPathPrefix = versioningPathPrefixRule.prefixRegex.find(this) ?: return this to this
        return this to removePrefix(versioningPathPrefix.value)
    }

    private fun violationForPath(pathName: String) = Violation(pathName, alphabet)

    private fun String.isTemplatedSegment(): Boolean = startsWith(pathTemplateStart) && endsWith(pathTemplateEnd)

    companion object {

        private const val pathTemplateStart = '{'
        private const val pathTemplateEnd = '}'
        private const val pathSegmentSeparator = '/'
    }

    data class Violation(val path: String, val alphabet: Set<Char>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Path $path should only contain characters in $alphabet but doesn't"
    }
}