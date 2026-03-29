package sollecitom.libs.swissknife.compliance.checker.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ComplianceRuleTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class ResultFactoryMethods {

        @Test
        fun `withViolations returns Compliant when violations are empty`() {

            val result = ComplianceRule.Result.withViolations<String>(emptySet())

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<String>>()
        }

        @Test
        fun `withViolations returns NonCompliant when violations are present`() {

            val violation = TestViolation("something is wrong")
            val result = ComplianceRule.Result.withViolations(setOf(violation))

            assertThat(result).isInstanceOf<ComplianceRule.Result.NonCompliant<String>>()
        }

        @Test
        fun `withViolationOrNull returns Compliant when violation is null`() {

            val result = ComplianceRule.Result.withViolationOrNull<String>(null)

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<String>>()
        }

        @Test
        fun `withViolationOrNull returns NonCompliant when violation is not null`() {

            val violation = TestViolation("something is wrong")
            val result = ComplianceRule.Result.withViolationOrNull(violation)

            assertThat(result).isInstanceOf<ComplianceRule.Result.NonCompliant<String>>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NonCompliantResult {

        @Test
        fun `creating NonCompliant with empty violations fails`() {

            val result = runCatching { ComplianceRule.Result.NonCompliant<String>(emptySet()) }

            assertThat(result.isFailure).isTrue()
        }

        @Test
        fun `NonCompliant contains the violations`() {

            val violation = TestViolation("problem")
            val result = ComplianceRule.Result.NonCompliant(violation)

            assertThat(result.violations).isEqualTo(setOf(violation))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class CompliantResult {

        @Test
        fun `two Compliant instances are equal`() {

            val result1 = ComplianceRule.Result.Compliant<String>()
            val result2 = ComplianceRule.Result.Compliant<String>()

            assertThat(result1).isEqualTo(result2)
        }
    }

    private data class TestViolation(override val message: String) : ComplianceRule.Result.Violation<String>
}
