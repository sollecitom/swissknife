package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.Compliant
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule.Result.NonCompliant
import sollecitom.libs.swissknife.json.utils.JsonSchema

data object DisallowRequiringUndeclaredPropertiesRule : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val requiredUndeclaredProperties = target.requiredPropertyNames.filter { it !in target.propertyNames }.toSet()
        if (requiredUndeclaredProperties.isNotEmpty()) return requiredUndeclaredProperties.nonCompliant()
        return Compliant()
    }

    private fun Set<String>.nonCompliant() = NonCompliant(Violation(this))

    data class Violation(val offendingProperties: Set<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema shouldn't require any properties it doesn't declare but does. The offending required properties are $offendingProperties"
    }
}