package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.parameters.RequestBody

class ForbiddenRequestBodyRule(private val methods: Set<HttpMethod>) : ComplianceRule<OpenAPI> {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().filter { it.operation.method in methods }.mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, requestBody!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody != null

    data class Violation(val operation: OperationWithContext, val requestBody: RequestBody) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} shouldn't specify a request body, but does"
    }
}