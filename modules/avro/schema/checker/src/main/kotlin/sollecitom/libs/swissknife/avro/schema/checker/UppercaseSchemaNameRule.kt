package sollecitom.libs.swissknife.avro.schema.checker

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema

data object UppercaseSchemaNameRule : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violation = check(target.name)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(schemaName: String): Violation? {

        if (!schemaName.first().isUpperCase()) return violation(schemaName)
        return null
    }

    private fun violation(schemaName: String) = Violation(schemaName = schemaName)

    data class Violation(val schemaName: String) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Schema name '$schemaName' should start with an uppercase character but doesn't"
    }
}