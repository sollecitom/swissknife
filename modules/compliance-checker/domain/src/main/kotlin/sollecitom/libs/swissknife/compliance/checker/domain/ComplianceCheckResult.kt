package sollecitom.libs.swissknife.compliance.checker.domain

sealed class ComplianceCheckResult<in TARGET : Any> {

    class Compliant<in TARGET : Any> : ComplianceCheckResult<TARGET>() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode() = javaClass.hashCode()

        override fun toString() = "Compliant"
    }

    data class NonCompliant<in TARGET : Any>(val problems: Set<ComplianceRule.Result.NonCompliant<TARGET>>) : ComplianceCheckResult<TARGET>() {

        constructor(problem: ComplianceRule.Result.NonCompliant<TARGET>) : this(problems = setOf(problem))

        fun description(): String = problems.joinToString(separator = "\n", prefix = "Compliance problems:\n") { problem ->
            problem.violations.joinToString(separator = "\n\t- ", prefix = "\t- ", transform = ComplianceRule.Result.Violation<TARGET>::message)
        }
    }
}