package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import io.swagger.v3.oas.models.OpenAPI


class WhitelistedOpenApiVersionFieldRule(val whitelistedOpenApiVersions: Set<String>) : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violation = check(target.openapi)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(version: String): Violation? {

        if (version !in whitelistedOpenApiVersions) {
            return Violation(version, whitelistedOpenApiVersions)
        }
        return null
    }

    data class Violation(val declaredVersion: String?, val whitelistedVersions: Set<String>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "OpenAPI version must be one of ${whitelistedVersions.joinToString(prefix = "[", postfix = "]")}, but was $declaredVersion"
    }
}