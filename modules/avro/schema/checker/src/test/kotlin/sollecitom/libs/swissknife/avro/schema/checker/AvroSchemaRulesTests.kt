package sollecitom.libs.swissknife.avro.schema.checker

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema
import org.junit.jupiter.api.Nested
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

    @Nested
    @TestInstance(PER_CLASS)
    inner class NullFirstNullableUnions {

        @Test
        fun `a nullable field listing null first and defaulting to null complies`() {

            val schema = record("""{"name":"value","type":["null","string"],"default":null}""")

            val result = NullFirstNullableUnionsRule(schema)

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
        }

        @Test
        fun `a nullable union not listing null first is a violation`() {

            val schema = record("""{"name":"value","type":["string","null"],"default":"none"}""")

            val result = NullFirstNullableUnionsRule(schema)

            assertThat(result).isEqualTo(nonCompliant(NullFirstNullableUnionsRule.Violation.NullNotFirst(path = "acme.Thing.value"), NullFirstNullableUnionsRule.Violation.MissingNullDefault(path = "acme.Thing.value")))
        }

        @Test
        fun `a nullable field without a null default is a violation`() {

            val schema = record("""{"name":"value","type":["null","string"]}""")

            val result = NullFirstNullableUnionsRule(schema)

            assertThat(result).isEqualTo(nonCompliant(NullFirstNullableUnionsRule.Violation.MissingNullDefault(path = "acme.Thing.value")))
        }

        @Test
        fun `nested nullable unions are checked too`() {

            val schema = record("""{"name":"items","type":{"type":"array","items":{"type":"record","name":"Item","fields":[{"name":"note","type":["null","string"]},{"name":"tags","type":{"type":"map","values":["string","null"]}}]}}}""")

            val result = NullFirstNullableUnionsRule(schema)

            assertThat(result).isEqualTo(nonCompliant(NullFirstNullableUnionsRule.Violation.MissingNullDefault(path = "acme.Thing.items[].note"), NullFirstNullableUnionsRule.Violation.NullNotFirst(path = "acme.Thing.items[].tags{}")))
        }

        @Test
        fun `a recursive schema is checked once per named type`() {

            val schema = Schema.Parser().parse("""{"type":"record","name":"Node","namespace":"acme","fields":[{"name":"next","type":["null","Node"],"default":null}]}""")

            val result = NullFirstNullableUnionsRule(schema)

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryEnumDefaultSymbol {

        private val rule = MandatoryEnumDefaultSymbolRule(symbol = "UNKNOWN")

        @Test
        fun `an enum declaring the symbol as its default complies`() {

            val schema = Schema.Parser().parse("""{"type":"enum","name":"Colour","namespace":"acme","symbols":["UNKNOWN","RED"],"default":"UNKNOWN"}""")

            val result = rule(schema)

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
        }

        @Test
        fun `an enum without the symbol is a violation`() {

            val result = rule(enumSchema)

            assertThat(result).isEqualTo(nonCompliant(MandatoryEnumDefaultSymbolRule.Violation(enumName = "acme.Colour", symbol = "UNKNOWN")))
        }

        @Test
        fun `an enum with the symbol but another default is a violation`() {

            val schema = Schema.Parser().parse("""{"type":"enum","name":"Colour","namespace":"acme","symbols":["UNKNOWN","RED"],"default":"RED"}""")

            val result = rule(schema)

            assertThat(result).isEqualTo(nonCompliant(MandatoryEnumDefaultSymbolRule.Violation(enumName = "acme.Colour", symbol = "UNKNOWN")))
        }

        @Test
        fun `an enum nested in a record is checked too`() {

            val schema = record("""{"name":"colour","type":{"type":"enum","name":"Colour","symbols":["RED"]}}""")

            val result = rule(schema)

            assertThat(result).isEqualTo(nonCompliant(MandatoryEnumDefaultSymbolRule.Violation(enumName = "acme.Colour", symbol = "UNKNOWN")))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class TopicEventUnion {

        private val rule = TopicEventUnionRule()

        @Test
        fun `a topic schema reaching a union of event records complies`() {

            val schema = record("""{"name":"data","type":{"type":"record","name":"ThingEventData","fields":[{"name":"envelope","type":[{"type":"record","name":"Created","fields":[]},{"type":"record","name":"Deleted","fields":[]}]}]}}""")

            val result = rule(schema)

            assertThat(result).isInstanceOf<ComplianceRule.Result.Compliant<Schema>>()
        }

        @Test
        fun `a topic schema holding a single event record is a violation`() {

            val schema = record("""{"name":"data","type":{"type":"record","name":"ThingEventData","fields":[{"name":"envelope","type":{"type":"record","name":"Created","fields":[]}}]}}""")

            val result = rule(schema)

            assertThat(result).isEqualTo(nonCompliant(TopicEventUnionRule.Violation(topicSchemaName = "acme.Thing", eventsFieldPath = listOf("data", "envelope"))))
        }

        @Test
        fun `a topic schema without the events path is a violation`() {

            val schema = record("""{"name":"payload","type":"string"}""")

            val result = rule(schema)

            assertThat(result).isEqualTo(nonCompliant(TopicEventUnionRule.Violation(topicSchemaName = "acme.Thing", eventsFieldPath = listOf("data", "envelope"))))
        }
    }

    private fun record(vararg fields: String) = Schema.Parser().parse("""{"type":"record","name":"Thing","namespace":"acme","fields":[${fields.joinToString(",")}]}""")

    private fun nonCompliant(vararg violations: ComplianceRule.Result.Violation<Schema>) = ComplianceRule.Result.NonCompliant(violations.toSet())
}
