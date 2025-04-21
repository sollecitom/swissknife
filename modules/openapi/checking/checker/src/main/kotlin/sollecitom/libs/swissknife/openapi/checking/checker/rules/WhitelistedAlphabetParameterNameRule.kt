package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.kotlin.extensions.text.capitalized
import sollecitom.libs.swissknife.openapi.checking.checker.model.ParameterLocation
import sollecitom.libs.swissknife.openapi.checking.checker.model.ParameterWithLocation
import sollecitom.libs.swissknife.openapi.checking.checker.model.allParameters
import io.swagger.v3.oas.models.OpenAPI


class WhitelistedAlphabetParameterNameRule(val pathAlphabet: Set<Char>, val headerAlphabet: Set<Char> = pathAlphabet, val queryAlphabet: Set<Char> = pathAlphabet, val cookieAlphabet: Set<Char> = pathAlphabet) : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allParameters().asSequence().mapNotNull { parameter -> check(parameter) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(parameter: ParameterWithLocation): Violation? {

        if (parameter.isNotCompliantWithNamingConvention()) return parameter.violation()
        return null
    }

    private val ParameterWithLocation.whitelistedAlphabet: Set<Char>
        get() = when (location) {
            is ParameterLocation.Path -> pathAlphabet
            is ParameterLocation.Cookie -> cookieAlphabet
            is ParameterLocation.Header -> headerAlphabet
            is ParameterLocation.Query -> queryAlphabet
        }

    private fun ParameterWithLocation.violation() = Violation(this, whitelistedAlphabet)

    private fun ParameterWithLocation.isNotCompliantWithNamingConvention(): Boolean = parameter.name.any { character -> character !in whitelistedAlphabet }

    data class Violation(val parameter: ParameterWithLocation, val whitelistedAlphabet: Set<Char>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "${parameter.location.value.capitalized()} parameter with name ${parameter.parameter.name} for operation ${parameter.location.operation.method} on path ${parameter.location.pathName} should only contain characters in $whitelistedAlphabet but doesn't"
    }
}