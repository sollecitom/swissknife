package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class WhitelistedAlphabetFieldNameRule(val alphabet: Set<Char>) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.flatMap { (path, schema) -> schema.properties.mapNotNull { check(it, path) } }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(property: JsonSchema.Property, path: List<String>): Violation? {

        if (property.name.any { character -> character !in alphabet }) return property.violation(path)
        return null
    }

    private fun JsonSchema.Property.violation(path: List<String>) = Violation(alphabet = alphabet, path = path + name)

    data class Violation(val alphabet: Set<Char>, val path: List<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "Field ${path.joinToString(".")} should only contain characters in $alphabet but doesn't"
    }

    companion object
}