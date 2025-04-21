package sollecitom.libs.swissknife.openapi.checking.test.utils

import assertk.Assert
import assertk.assertions.hasSize
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.rule.field.FieldRulesViolation
import io.swagger.v3.oas.models.OpenAPI

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : ComplianceRule.Result.Violation<OpenAPI>, VALUE : Any> Assert<FieldRulesViolation<VALUE>>.hasSingleFieldViolation(check: (VIOLATION) -> Unit) = given { violation ->

    assertThat(violation.fieldViolations).hasSize(1)
    val fieldViolation = violation.fieldViolations.single() as VIOLATION
    check(fieldViolation)
}