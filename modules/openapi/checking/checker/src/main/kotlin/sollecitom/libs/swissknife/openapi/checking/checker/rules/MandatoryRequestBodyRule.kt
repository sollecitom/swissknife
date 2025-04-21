package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import sollecitom.libs.swissknife.openapi.checking.checker.model.isRequired
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod


class MandatoryRequestBodyRule(methods: Set<Pair<HttpMethod, Boolean>>) : ComplianceRule<OpenAPI> {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    private val methodsToCheck = methods.asSequence().map { it.first }.toSet()
    private val isBodyRequiredByMethod = methods.toMap()

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().filter { it.operation.method in methodsToCheck }.mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, isBodyRequiredByMethod[method]!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody == null || (!requestBody.isRequired() && isBodyRequiredByMethod[operation.method]!!)

    data class Violation(val operation: OperationWithContext, val requiredBody: Boolean) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify ${if (requiredBody) "a required" else "an optional"} request body, but doesn't"
    }
}