package sollecitom.libs.swissknife.openapi.checking.checker.rule.field

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import io.swagger.v3.oas.models.OpenAPI

fun interface FieldRule<in VALUE, out VIOLATION : ComplianceRule.Result.Violation<OpenAPI>> {

    fun check(value: VALUE, api: OpenAPI): VIOLATION?
}