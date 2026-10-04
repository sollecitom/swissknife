package sollecitom.libs.swissknife.avro.schema.checker

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AvroSchemaRulesTests {

    private val schemaWithoutNamespace = Schema.Parser().parse("""{"type":"record","name":"Thing","fields":[{"name":"value","type":"string"}]}""")
    private val enumSchema = Schema.Parser().parse("""{"type":"enum","name":"Colour","namespace":"acme","symbols":["RED","GREEN"]}""")

    @Test
    fun `a schema without a namespace violates the mandatory namespace prefix rule`() {

        val result = MandatoryNamespacePrefixRule(prefix = "acme")(schemaWithoutNamespace)

        assertThat(result).isEqualTo(ComplianceRule.Result.NonCompliant(MandatoryNamespacePrefixRule.Violation(namespace = "", prefix = "acme")))
    }

    @Test
    fun `a schema without a namespace complies with the whitelisted alphabet namespace rule`() {

        val result = WhitelistedAlphabetNamespaceNameRule(alphabet = ('a'..'z').toSet())(schemaWithoutNamespace)

        assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
    }

    @Test
    fun `an enum schema complies with the whitelisted alphabet field name rule`() {

        val result = WhitelistedAlphabetFieldNameRule(alphabet = ('a'..'z').toSet())(enumSchema)

        assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
    }
}
