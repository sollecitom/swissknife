package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema
import com.github.erosb.jsonsKema.ConstSchema

data object DisallowConstKeywordRule : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val propertiesWithConst = target.properties.mapNotNull { property -> property.schema.value.subschemas().filterIsInstance<ConstSchema>().singleOrNull()?.let { property.name } }.toSet()
        if (propertiesWithConst.isNotEmpty()) return propertiesWithConst.nonCompliant()
        return Compliant()
    }

    private fun Set<String>.nonCompliant() = NonCompliant(Violation(this))

    data class Violation(val offendingProperties: Set<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema shouldn't declare any properties that restrict the value of a property with the 'const' keyword, but does (use 'enum' instead). The offending required properties are $offendingProperties"
    }
}