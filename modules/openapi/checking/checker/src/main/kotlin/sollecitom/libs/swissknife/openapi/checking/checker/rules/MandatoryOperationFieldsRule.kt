package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiField
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import sollecitom.libs.swissknife.openapi.checking.checker.rules.utils.trimmed
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation

class MandatoryOperationFieldsRule(private val requiredFields: Set<OpenApiField<Operation, Any?>>) : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        val missingRequiredFields = requiredFields.filter { field -> field.getter(operation.operation.operation)?.trimmed() == null }.toSet()
        if (missingRequiredFields.isNotEmpty()) return operation.violation(missingRequiredFields)
        return null
    }

    private fun OperationWithContext.violation(missingRequiredFields: Set<OpenApiField<Operation, Any?>>) = Violation(this, requiredFields, missingRequiredFields)

    private fun OperationWithContext.isNotCompliant(): Boolean = requiredFields.any { field -> field.getter(operation.operation)?.trimmed() == null }

    data class Violation(val operation: OperationWithContext, val requiredFields: Set<OpenApiField<Operation, Any?>>, val missingRequiredFields: Set<OpenApiField<Operation, Any?>>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify the following mandatory fields ${requiredFields.map(OpenApiField<Operation, *>::name)}, but fields ${missingRequiredFields.map(OpenApiField<Operation, *>::name)} were missing"
    }
}