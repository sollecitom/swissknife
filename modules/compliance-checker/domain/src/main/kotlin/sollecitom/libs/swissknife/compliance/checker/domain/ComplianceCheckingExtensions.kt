package sollecitom.libs.swissknife.compliance.checker.domain

fun <TARGET : Any> TARGET.checkAgainstRules(vararg rules: ComplianceRule<TARGET>): ComplianceCheckResult<TARGET> = checkAgainstRules(rules.toSet())

fun <TARGET : Any> TARGET.checkAgainstRules(rules: Set<ComplianceRule<TARGET>>): ComplianceCheckResult<TARGET> {

    val checker = ComplianceChecker.withRules(rules)
    return checker.check(this)
}

fun <TARGET : Any> TARGET.checkAgainstRules(firstRuleSet: ComplianceRuleSet<TARGET>, vararg otherRuleSets: ComplianceRuleSet<TARGET>) = checkAgainstRules(rules = firstRuleSet.rules + otherRuleSets.flatMap(ComplianceRuleSet<TARGET>::rules).toSet())