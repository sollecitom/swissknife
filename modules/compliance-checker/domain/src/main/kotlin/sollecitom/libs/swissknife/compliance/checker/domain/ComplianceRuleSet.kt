package sollecitom.libs.swissknife.compliance.checker.domain

interface ComplianceRuleSet<in TARGET : Any> {

    val rules: Set<ComplianceRule<TARGET>>
}