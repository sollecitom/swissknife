package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod

class MandatoryRequestBodyContentMediaTypesRule(private val methodsToCheck: Set<HttpMethod>) : ComplianceRule<OpenAPI> {

    init {
        require(methodsToCheck.isNotEmpty()) { "Must specify at least one method to check" }
    }

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().filter { it.operation.method in methodsToCheck }.mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, methodsToCheck)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody != null && requestBody.content.entries.isEmpty()

    data class Violation(val operation: OperationWithContext, val methodsToCheck: Set<HttpMethod>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify at least a media type for the content of the request body, but doesn't"
    }
}