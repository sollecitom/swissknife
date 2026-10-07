package sollecitom.libs.swissknife.json.utils.checker

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.DisallowConstKeywordRule
import sollecitom.libs.swissknife.json.utils.checker.rules.DisallowRequiringUndeclaredPropertiesRule
import sollecitom.libs.swissknife.json.utils.checker.rules.EnforcedAdditionalPropertiesValueRule
import sollecitom.libs.swissknife.json.utils.checker.rules.MandatoryAdditionalPropertiesRule
import sollecitom.libs.swissknife.json.utils.checker.rules.WhitelistedAlphabetFieldNameRule
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class NestedJsonSchemaRulesTests {

    @Test
    fun `a nested field name outside the alphabet is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": false, "properties": {"bad_name": {"type": "string"}}}""")
        val rule = WhitelistedAlphabetFieldNameRule(alphabet = ('a'..'z').toSet() + '-')

        val result = schema.checkAgainstRules(rule)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetFieldNameRule.Violation, JsonSchema> { violation ->
            assertThat(violation.property.name).isEqualTo("bad_name")
            assertThat(violation.path).isEqualTo(listOf("outer", "bad_name"))
        }
    }

    @Test
    fun `a nested object without additionalProperties is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object", "properties": {"name": {"type": "string"}}}""")

        val result = schema.checkAgainstRules(MandatoryAdditionalPropertiesRule(affectPureUnionTypes = false))

        assertThat(result).isNotCompliantWithOnlyViolation(MandatoryAdditionalPropertiesRule.Violation(listOf("outer")))
    }

    @Test
    fun `a nested object allowing additional properties is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": true, "properties": {"name": {"type": "string"}}}""")

        val result = schema.checkAgainstRules(EnforcedAdditionalPropertiesValueRule(enforcedValue = false, affectPureUnionTypes = false))

        assertThat(result).isNotCompliantWithOnlyViolation(EnforcedAdditionalPropertiesValueRule.Violation(value = false, path = listOf("outer")))
    }

    @Test
    fun `a nested const is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": false, "properties": {"kind": {"const": "a"}}}""")

        val result = schema.checkAgainstRules(DisallowConstKeywordRule)

        assertThat(result).isNotCompliantWithOnlyViolation(DisallowConstKeywordRule.Violation(listOf("outer", "kind")))
    }

    @Test
    fun `a nested undeclared required property is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": false, "required": ["missing"], "properties": {"name": {"type": "string"}}}""")

        val result = schema.checkAgainstRules(DisallowRequiringUndeclaredPropertiesRule)

        assertThat(result).isNotCompliantWithOnlyViolation(DisallowRequiringUndeclaredPropertiesRule.Violation(listOf("outer", "missing")))
    }

    @Test
    fun `a nested object without properties and without additionalProperties is reported with its path`() {

        val schema = objectWithNested(nested = """{"type": "object"}""")

        val result = schema.checkAgainstRules(MandatoryAdditionalPropertiesRule(affectPureUnionTypes = false))

        assertThat(result).isNotCompliantWithOnlyViolation(MandatoryAdditionalPropertiesRule.Violation(listOf("outer")))
    }

    @Test
    fun `a nested map declaring an additionalProperties schema is compliant`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": {"type": "string"}}""")

        val result = schema.checkAgainstRules(MandatoryAdditionalPropertiesRule(affectPureUnionTypes = false))

        assertThat(result).isCompliant()
    }

    @Test
    fun `objects nested in array items are checked`() {

        val schema = JSONObject("""{"type": "object", "additionalProperties": false, "properties": {"list": {"type": "array", "items": {"type": "object", "properties": {"name": {"type": "string"}}}}}}""").asSchema()

        val result = schema.checkAgainstRules(MandatoryAdditionalPropertiesRule(affectPureUnionTypes = false))

        assertThat(result).isNotCompliantWithOnlyViolation(MandatoryAdditionalPropertiesRule.Violation(listOf("list", JsonSchema.ITEMS_PATH_SEGMENT)))
    }

    @Test
    fun `compliant nested objects pass`() {

        val schema = objectWithNested(nested = """{"type": "object", "additionalProperties": false, "properties": {"name": {"type": "string"}}}""")

        val result = schema.checkAgainstRules(MandatoryAdditionalPropertiesRule(affectPureUnionTypes = false))

        assertThat(result).isCompliant()
    }

    private fun objectWithNested(nested: String): JsonSchema = JSONObject("""{"type": "object", "additionalProperties": false, "properties": {"outer": $nested}}""").asSchema()
}
