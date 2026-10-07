package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class MandatoryAdditionalPropertiesRule(val affectPureUnionTypes: Boolean) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.filter { (_, schema) -> schema.isAffected() && !schema.declaresAdditionalProperties }.map { (path, _) -> Violation(path) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun JsonSchema.isAffected(): Boolean = affectPureUnionTypes || !isAPureUnionType

    data class Violation(val path: List<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema${path.location()} should declare \"additionalProperties\" but doesn't"
    }
}