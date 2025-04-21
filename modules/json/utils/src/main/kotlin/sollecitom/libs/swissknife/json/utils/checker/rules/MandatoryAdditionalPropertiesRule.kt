package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class MandatoryAdditionalPropertiesRule(val affectPureUnionTypes: Boolean) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        if (target.isAffected() && target.allowsAdditionalProperties == null) return NonCompliant(violation = Violation)
        return Compliant()
    }

    private fun JsonSchema.isAffected(): Boolean = affectPureUnionTypes || !isAPureUnionType

    data object Violation : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema should declare \"additionalProperties\" but doesn't"
    }
}