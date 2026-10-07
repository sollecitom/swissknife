package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema

data object DisallowRequiringUndeclaredPropertiesRule : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.flatMap { (path, schema) -> schema.requiredPropertyNames.filter { it !in schema.propertyNames }.map { Violation(path + it) } }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    data class Violation(val path: List<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema shouldn't require a property it doesn't declare, but requires ${path.joinToString(".")}"
    }
}