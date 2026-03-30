package sollecitom.libs.swissknife.compliance.checker.domain

/** A named group of [ComplianceRule]s that can be applied together. */
interface ComplianceRuleSet<in TARGET : Any> {

    val rules: Set<ComplianceRule<TARGET>>
}