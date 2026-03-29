package sollecitom.libs.swissknife.compliance.checker.domain

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ComplianceCheckResultTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class CompliantEquals {

        @Test
        fun `two Compliant instances are equal`() {

            val result1 = ComplianceCheckResult.Compliant<String>()
            val result2 = ComplianceCheckResult.Compliant<String>()

            assertThat(result1).isEqualTo(result2)
        }

        @Test
        fun `Compliant toString returns Compliant`() {

            val result = ComplianceCheckResult.Compliant<String>()

            assertThat(result.toString()).isEqualTo("Compliant")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NonCompliantDescription {

        @Test
        fun `description includes violation messages`() {

            val violation = TestViolation("field is required")
            val problem = ComplianceRule.Result.NonCompliant(violation)
            val result = ComplianceCheckResult.NonCompliant(problem)

            val description = result.description()

            assertThat(description).contains("field is required")
        }

        @Test
        fun `description includes multiple violation messages`() {

            val violation1 = TestViolation("field is required")
            val violation2 = TestViolation("value too long")
            val problem = ComplianceRule.Result.NonCompliant(setOf(violation1, violation2))
            val result = ComplianceCheckResult.NonCompliant(problem)

            val description = result.description()

            assertThat(description).contains("field is required")
            assertThat(description).contains("value too long")
        }
    }

    private data class TestViolation(override val message: String) : ComplianceRule.Result.Violation<String>
}
