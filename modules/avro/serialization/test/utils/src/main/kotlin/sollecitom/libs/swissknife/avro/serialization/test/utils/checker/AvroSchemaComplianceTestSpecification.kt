package sollecitom.libs.swissknife.avro.serialization.test.utils.checker

import assertk.assertThat
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliantWith
import org.apache.avro.Schema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface AvroSchemaComplianceTestSpecification {

    val avroSchema: Schema
    val complianceRules: ComplianceRuleSet<Schema>

    @Test
    fun `avro schema complies with the specified compliance rules`() {

        assertThat(avroSchema).isCompliantWith(complianceRules)
    }
}