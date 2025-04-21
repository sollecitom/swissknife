package sollecitom.libs.swissknife.compliance.checker.domain

fun interface ComplianceRule<in TARGET : Any> {

    operator fun invoke(target: TARGET): Result<TARGET>

    sealed class Result<in TARGET : Any> {

        class Compliant<in TARGET : Any> : Result<TARGET>() {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return true
            }

            override fun hashCode() = javaClass.hashCode()

            override fun toString() = "Compliant"
        }

        data class NonCompliant<in TARGET : Any>(val violations: Set<Violation<TARGET>>) : Result<TARGET>() {

            constructor(violation: Violation<TARGET>) : this(violations = setOf(violation))

            init {
                require(violations.isNotEmpty())
            }

            override fun toString() = "violations=$violations"
        }

        companion object {

            fun <TARGET : Any> withViolations(violations: Set<Violation<TARGET>>): Result<TARGET> = if (violations.isEmpty()) Compliant() else NonCompliant(violations)
            fun <TARGET : Any> withViolationOrNull(violation: Violation<TARGET>?): Result<TARGET> = if (violation == null) Compliant() else NonCompliant(setOf(violation))
        }

        interface Violation<in TARGET : Any> {
            val message: String
        }
    }
}