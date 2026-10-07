package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class EnforcedAdditionalPropertiesValueRule(val enforcedValue: Boolean, val affectPureUnionTypes: Boolean) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.filter { (_, schema) -> schema.isAffected() && schema.allowsAdditionalProperties != null && schema.allowsAdditionalProperties != enforcedValue }.map { (path, _) -> Violation(value = enforcedValue, path = path) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun JsonSchema.isAffected(): Boolean = affectPureUnionTypes || !isAPureUnionType

    data class Violation(val value: Boolean, val path: List<String> = emptyList()) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema${if (path.isEmpty()) "" else " at ${path.joinToString(".")}"} should declare \"additionalProperties: $value\" but doesn't"
    }

    companion object
}