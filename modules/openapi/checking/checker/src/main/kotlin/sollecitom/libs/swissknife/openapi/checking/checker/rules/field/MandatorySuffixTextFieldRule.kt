package sollecitom.libs.swissknife.openapi.checking.checker.rules.field

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.rule.field.FieldRule
import io.swagger.v3.oas.models.OpenAPI

data class MandatorySuffixTextFieldRule(val suffix: String, val ignoreCase: Boolean) : FieldRule<String, MandatorySuffixTextFieldRule.Violation> {

    override fun check(value: String, api: OpenAPI): Violation? {

        if (!value.endsWith(suffix = suffix, ignoreCase = ignoreCase)) return Violation(value, suffix, ignoreCase)
        return null
    }

    data class Violation(val value: String, val suffix: String, val ignoreCase: Boolean) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Value \"${value}\" should end with \"${suffix}\" ${if (ignoreCase) "case-insensitive" else "case-sensitive"}, but doesn't"
    }
}