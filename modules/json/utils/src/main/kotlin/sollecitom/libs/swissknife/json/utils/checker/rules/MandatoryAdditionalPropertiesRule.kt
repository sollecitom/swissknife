package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class MandatoryAdditionalPropertiesRule(val affectPureUnionTypes: Boolean) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.filter { (_, schema) -> schema.isAffected() && schema.allowsAdditionalProperties == null }.map { (path, _) -> if (path.isEmpty()) Violation else NestedViolation(path) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun JsonSchema.isAffected(): Boolean = affectPureUnionTypes || !isAPureUnionType

    data object Violation : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema should declare \"additionalProperties\" but doesn't"
    }

    data class NestedViolation(val path: List<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema at ${path.joinToString(".")} should declare \"additionalProperties\" but doesn't"
    }
}