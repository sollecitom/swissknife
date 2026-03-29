package sollecitom.libs.swissknife.compliance.checker.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RulesBasedComplianceCheckerTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class WithPassingRules {

        @Test
        fun `returns Compliant when all rules pass`() {

            val rule = ComplianceRule<String> { ComplianceRule.Result.Compliant() }
            val checker = ComplianceChecker.withRules(setOf(rule))

            val result = checker.check("test")

            assertThat(result).isInstanceOf<ComplianceCheckResult.Compliant<String>>()
        }

        @Test
        fun `returns Compliant when no rules are provided`() {

            val checker = ComplianceChecker.withRules<String>(emptySet())

            val result = checker.check("test")

            assertThat(result).isInstanceOf<ComplianceCheckResult.Compliant<String>>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WithFailingRules {

        @Test
        fun `returns NonCompliant when a rule fails`() {

            val violation = TestViolation("must not be blank")
            val rule = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation) }
            val checker = ComplianceChecker.withRules(setOf(rule))

            val result = checker.check("test")

            assertThat(result).isInstanceOf<ComplianceCheckResult.NonCompliant<String>>()
        }

        @Test
        fun `collects all violations from multiple failing rules`() {

            val violation1 = TestViolation("too short")
            val violation2 = TestViolation("must start with uppercase")
            val rule1 = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation1) }
            val rule2 = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation2) }
            val checker = ComplianceChecker.withRules(setOf(rule1, rule2))

            val result = checker.check("test")

            assertThat(result).isInstanceOf<ComplianceCheckResult.NonCompliant<String>>()
            val nonCompliant = result as ComplianceCheckResult.NonCompliant
            assertThat(nonCompliant.problems.size).isEqualTo(2)
        }

        @Test
        fun `only collects violations from failing rules`() {

            val violation = TestViolation("too short")
            val passingRule = ComplianceRule<String> { ComplianceRule.Result.Compliant() }
            val failingRule = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation) }
            val checker = ComplianceChecker.withRules(setOf(passingRule, failingRule))

            val result = checker.check("test")

            assertThat(result).isInstanceOf<ComplianceCheckResult.NonCompliant<String>>()
            val nonCompliant = result as ComplianceCheckResult.NonCompliant
            assertThat(nonCompliant.problems.size).isEqualTo(1)
        }
    }

    private data class TestViolation(override val message: String) : ComplianceRule.Result.Violation<String>
}
