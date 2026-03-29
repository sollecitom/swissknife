package sollecitom.libs.swissknife.compliance.checker.domain

import assertk.assertThat
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ComplianceCheckingExtensionsTests {

    @Test
    fun `checkAgainstRules returns Compliant when all rules pass`() {

        val rule = ComplianceRule<String> { ComplianceRule.Result.Compliant() }

        val result = "test".checkAgainstRules(rule)

        assertThat(result).isInstanceOf<ComplianceCheckResult.Compliant<String>>()
    }

    @Test
    fun `checkAgainstRules returns NonCompliant when a rule fails`() {

        val violation = TestViolation("invalid")
        val rule = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation) }

        val result = "test".checkAgainstRules(rule)

        assertThat(result).isInstanceOf<ComplianceCheckResult.NonCompliant<String>>()
    }

    @Test
    fun `checkAgainstRules with rule set delegates to rules`() {

        val violation = TestViolation("invalid")
        val rule = ComplianceRule<String> { ComplianceRule.Result.NonCompliant(violation) }
        val ruleSet = object : ComplianceRuleSet<String> {
            override val rules = setOf(rule)
        }

        val result = "test".checkAgainstRules(ruleSet)

        assertThat(result).isInstanceOf<ComplianceCheckResult.NonCompliant<String>>()
    }

    private data class TestViolation(override val message: String) : ComplianceRule.Result.Violation<String>
}
