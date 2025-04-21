package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema

data class WhitelistedAlphabetFieldNameRule(val alphabet: Set<Char>) : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.properties.mapNotNull(::check).toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(property: JsonSchema.Property): Violation? {

        if (property.name.any { character -> character !in alphabet }) return property.violation()
        return null
    }

    private fun JsonSchema.Property.violation() = Violation(property = this, alphabet = alphabet)

    data class Violation(val property: JsonSchema.Property, val alphabet: Set<Char>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "Field ${property.name} should only contain characters in $alphabet but doesn't"
    }

    companion object
}