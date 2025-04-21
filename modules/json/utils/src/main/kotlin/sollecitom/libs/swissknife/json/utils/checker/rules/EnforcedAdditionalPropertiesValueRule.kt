package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class EnforcedAdditionalPropertiesValueRule(val enforcedValue: Boolean, val affectPureUnionTypes: Boolean) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        if (target.isAffected() && target.allowsAdditionalProperties != null && target.allowsAdditionalProperties != enforcedValue) return NonCompliant(violation = target.violation())
        return Compliant()
    }

    private fun JsonSchema.isAffected(): Boolean = affectPureUnionTypes || !isAPureUnionType

    private fun JsonSchema.violation() = Violation(value = enforcedValue)

    data class Violation(val value: Boolean) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema should declare \"additionalProperties: $value\" but doesn't"
    }

    companion object
}