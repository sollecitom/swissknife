package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import io.swagger.v3.oas.models.OpenAPI

object LowercasePathNameRule : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.paths.asSequence().mapNotNull { (pathName, _) -> check(pathName) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(pathName: String): Violation? = pathName.takeIf { it.lowercase() != it }?.let { Violation(it) }

    data class Violation(val path: String) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Path $path should be lowercase but isn't"
    }
}