package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import io.swagger.v3.oas.models.OpenAPI

object EnforceCamelCaseOperationIdRule : ComplianceRule<OpenAPI> {

    private const val camelCasePattern = "^[a-z][a-z0-9]*(([A-Z][a-z0-9]+)*[A-Z]?|([a-z0-9]+[A-Z])*|[A-Z])$"
    private val regex = camelCasePattern.toRegex()

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this)

    private fun OperationWithContext.isNotCompliant(): Boolean = !operationId.isNullOrBlank() && !operationId!!.isAssignableAsFunctionName()

    private fun String.isAssignableAsFunctionName(): Boolean = regex.matches(this)

    data class Violation(val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should have an operationId in camelCase, but doesn't"
    }
}