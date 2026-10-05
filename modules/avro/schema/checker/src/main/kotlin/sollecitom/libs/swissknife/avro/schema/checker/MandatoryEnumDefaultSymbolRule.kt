package sollecitom.libs.swissknife.avro.schema.checker

import org.apache.avro.Schema
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule

/** Compliance rule that ensures every enum, anywhere in the schema, contains the given [symbol] and declares it as its default. */
data class MandatoryEnumDefaultSymbolRule(val symbol: String) : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violations = target.nodes().filter { it.schema.type == Schema.Type.ENUM && !it.schema.hasDefaultSymbol() }.map { Violation(enumName = it.schema.fullName, symbol = symbol) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun Schema.hasDefaultSymbol() = hasEnumSymbol(symbol) && enumDefault == symbol

    data class Violation(val enumName: String, val symbol: String) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Enum '$enumName' should contain the symbol '$symbol' and declare it as its default, but doesn't"
    }
}
