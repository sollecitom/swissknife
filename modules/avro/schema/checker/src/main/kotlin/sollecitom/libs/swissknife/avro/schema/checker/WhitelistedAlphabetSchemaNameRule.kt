package sollecitom.libs.swissknife.avro.schema.checker

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema

data class WhitelistedAlphabetSchemaNameRule(val alphabet: Set<Char>) : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violation = check(target.name)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(schemaName: String): Violation? {

        if (schemaName.any { character -> character !in alphabet }) return violation(schemaName)
        return null
    }

    private fun violation(schemaName: String) = Violation(schemaName = schemaName, alphabet = alphabet)

    data class Violation(val schemaName: String, val alphabet: Set<Char>) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Schema name '$schemaName' should only contain characters in $alphabet but doesn't"
    }

    companion object
}