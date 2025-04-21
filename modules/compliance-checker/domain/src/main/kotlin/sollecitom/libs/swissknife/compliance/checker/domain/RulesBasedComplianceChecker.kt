package sollecitom.libs.swissknife.compliance.checker.domain

internal class RulesBasedComplianceChecker<in TARGET : Any>(private val rules: Set<ComplianceRule<TARGET>>) : ComplianceChecker<TARGET> {

    override fun check(target: TARGET): ComplianceCheckResult<TARGET> {

        val problems = mutableSetOf<ComplianceRule.Result.NonCompliant<TARGET>>()
        rules.forEach { rule ->
            val result = rule(target)
            if (result is ComplianceRule.Result.NonCompliant) {
                problems += result
            }
        }
        return if (problems.isEmpty()) ComplianceCheckResult.Compliant() else ComplianceCheckResult.NonCompliant(problems)
    }
}

fun <TARGET : Any> ComplianceChecker.Companion.withRules(rules: Set<ComplianceRule<TARGET>>): ComplianceChecker<TARGET> = RulesBasedComplianceChecker(rules)