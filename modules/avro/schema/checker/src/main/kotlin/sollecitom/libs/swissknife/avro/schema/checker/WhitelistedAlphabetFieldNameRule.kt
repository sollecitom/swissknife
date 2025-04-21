package sollecitom.libs.swissknife.avro.schema.checker

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema

data class WhitelistedAlphabetFieldNameRule(val alphabet: Set<Char>) : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violations = target.fields.mapNotNull(::check).toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(property: Schema.Field): Violation? {

        if (property.name().any { character -> character !in alphabet }) return property.violation()
        return null
    }

    private fun Schema.Field.violation() = Violation(field = this, alphabet = alphabet)

    data class Violation(val field: Schema.Field, val alphabet: Set<Char>) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Field ${field.name()} should only contain characters in $alphabet but doesn't"
    }

    companion object
}