package sollecitom.libs.swissknife.compliance.checker.test.utils

import assertk.Assert
import assertk.assertions.hasSize
import assertk.fail
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceCheckResult
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.test.utils.assertions.containsSameElementsAs

fun <TARGET : Any> Assert<TARGET>.isCompliantWith(firstRuleSet: ComplianceRuleSet<TARGET>, vararg otherRuleSets: ComplianceRuleSet<TARGET>) = given { target ->

    val result = target.checkAgainstRules(firstRuleSet, *otherRuleSets)
    assertThat(result).isCompliant()
}

fun <TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isCompliant() = given { result ->

    if (result is ComplianceCheckResult.NonCompliant) {
        fail("Result should be compliant but isn't.\n${result.description()}")
    }
}

fun <TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliant() = given { result ->

    if (result is ComplianceCheckResult.Compliant) {
        fail("Result should not be compliant but is.")
    }
}

fun <TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliantWithOnlyViolation(expectedViolation: ComplianceRule.Result.Violation<TARGET>) = isNotCompliantWithViolations(expectedViolation)

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : ComplianceRule.Result.Violation<TARGET>, TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliantWithOnlyViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isNotCompliant()
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        assertThat(violations).hasSize(1)
        violations.single().let { it as VIOLATION }.apply(check)
    }
}

inline fun <reified VIOLATION : ComplianceRule.Result.Violation<TARGET>, TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliantWithViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isNotCompliant()
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        violations.filterIsInstance<VIOLATION>().single().apply(check)
    }
}

fun <TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliantWithViolations(vararg expectedViolations: ComplianceRule.Result.Violation<TARGET>) = isNotCompliantWithViolations(expectedViolations.toSet())

fun <TARGET : Any> Assert<ComplianceCheckResult<TARGET>>.isNotCompliantWithViolations(expectedViolations: Set<ComplianceRule.Result.Violation<TARGET>>) = given { result ->

    assertThat(result).isNotCompliant()
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        assertThat(violations.toSet()).containsSameElementsAs(expectedViolations)
    }
}