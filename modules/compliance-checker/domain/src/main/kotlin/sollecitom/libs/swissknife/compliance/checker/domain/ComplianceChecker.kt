package sollecitom.libs.swissknife.compliance.checker.domain

/** Checks a target against a set of compliance rules. Use [ComplianceChecker.withRules] to create from rules. */
fun interface ComplianceChecker<in TARGET : Any> {

    fun check(target: TARGET): ComplianceCheckResult<TARGET>

    companion object
}