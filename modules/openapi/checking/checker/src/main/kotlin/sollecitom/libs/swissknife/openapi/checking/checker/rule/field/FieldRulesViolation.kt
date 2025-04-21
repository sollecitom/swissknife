package sollecitom.libs.swissknife.openapi.checking.checker.rule.field

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiField
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation

data class FieldRulesViolation<VALUE : Any>(val operation: OperationWithContext, val field: OpenApiField<Operation, VALUE?>, val rules: Set<FieldRule<*, *>>, val fieldViolations: Set<ComplianceRule.Result.Violation<OpenAPI>>) : ComplianceRule.Result.Violation<OpenAPI> {

    override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should comply with rules $rules but doesn't. Violations where: ${fieldViolations.map(ComplianceRule.Result.Violation<OpenAPI>::message).joinToString("\n\t")}"
}