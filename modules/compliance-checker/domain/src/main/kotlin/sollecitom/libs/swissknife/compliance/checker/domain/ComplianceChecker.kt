package sollecitom.libs.swissknife.compliance.checker.domain

fun interface ComplianceChecker<in TARGET : Any> {

    fun check(target: TARGET): ComplianceCheckResult<TARGET>

    companion object
}