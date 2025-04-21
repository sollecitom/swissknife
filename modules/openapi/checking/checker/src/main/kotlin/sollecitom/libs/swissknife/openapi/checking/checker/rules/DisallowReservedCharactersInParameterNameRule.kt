package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.kotlin.extensions.text.capitalized
import sollecitom.libs.swissknife.openapi.checking.checker.model.ParameterWithLocation
import sollecitom.libs.swissknife.openapi.checking.checker.model.allParameters
import io.swagger.v3.oas.models.OpenAPI

object DisallowReservedCharactersInParameterNameRule : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allParameters().asSequence().mapNotNull { parameter -> check(parameter) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(parameter: ParameterWithLocation): Violation? {

        if (parameter.isNotCompliant()) return parameter.violation()
        return null
    }

    private fun ParameterWithLocation.violation() = Violation(this)

    private fun ParameterWithLocation.isNotCompliant(): Boolean = (parameter.allowReserved ?: false)

    data class Violation(val parameter: ParameterWithLocation) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "${parameter.location.value.capitalized()} parameter with name ${parameter.parameter.name} for operation ${parameter.location.operation.method} on path ${parameter.location.pathName} shouldn't allow reserved URL characters but does"
    }
}