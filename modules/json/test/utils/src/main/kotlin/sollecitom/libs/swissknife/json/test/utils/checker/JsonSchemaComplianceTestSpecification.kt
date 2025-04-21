package sollecitom.libs.swissknife.json.test.utils.checker

import assertk.assertThat
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliantWith
import sollecitom.libs.swissknife.json.utils.JsonSchema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface JsonSchemaComplianceTestSpecification {

    val jsonSchema: JsonSchema
    val complianceRules: ComplianceRuleSet<JsonSchema>

    @Test
    fun `json schema complies with the specified compliance rules`() {

        assertThat(jsonSchema).isCompliantWith(complianceRules)
    }
}