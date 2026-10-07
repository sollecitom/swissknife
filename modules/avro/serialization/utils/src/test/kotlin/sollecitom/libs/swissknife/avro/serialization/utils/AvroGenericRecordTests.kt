package sollecitom.libs.swissknife.avro.serialization.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import sollecitom.libs.swissknife.avro.serialization.test.utils.serializeAndDeserializeWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.math.BigDecimal
import java.math.BigInteger

@TestInstance(PER_CLASS)
class AvroGenericRecordTests {

    private val itemSchema = Schema.Parser().parse("""{"type":"record","name":"Item","fields":[{"name":"label","type":"string"}]}""")
    private val schema = Schema.Parser().parse(
        """
        {"type":"record","name":"Container","fields":[
            {"name":"items","type":["null",{"type":"array","items":{"type":"record","name":"Item","fields":[{"name":"label","type":"string"}]}}],"default":null},
            {"name":"amount","type":"string"},
            {"name":"count","type":"string"}
        ]}
        """.trimIndent()
    )

    @Test
    fun `setting records on a nullable array field`() {

        val items = listOf(buildGenericRecord(itemSchema) { set("label", "a") }, buildGenericRecord(itemSchema) { set("label", "b") })

        val record = buildGenericRecord(schema) {
            setRecords("items", items)
            set("amount", BigDecimal("1.50"))
            set("count", BigInteger("42"))
        }

        assertThat(record.getRecordList("items").map { it.getString("label") }).isEqualTo(listOf("a", "b"))
    }

    @Test
    fun `reading big numbers stored in string fields after a binary round trip`() {

        val record = buildGenericRecord(schema) {
            set("amount", BigDecimal("1.50"))
            set("count", BigInteger("42"))
        }

        val roundTripped = record.serializeAndDeserializeWith(schema)

        assertThat(roundTripped.getBigDecimal("amount")).isEqualTo(BigDecimal("1.50"))
        assertThat(roundTripped.getBigInteger("count")).isEqualTo(BigInteger("42"))
    }

    @Test
    fun `reading big numbers from a record that was not serialized`() {

        val record = buildGenericRecord(schema) {
            set("amount", BigDecimal("1.50"))
            set("count", BigInteger("42"))
        }

        assertThat(record.getBigDecimal("amount")).isEqualTo(BigDecimal("1.50"))
        assertThat(record.getBigInteger("count")).isEqualTo(BigInteger("42"))
    }

    @Test
    fun `big numbers in decimal bytes fields survive a binary round trip`() {

        val decimalSchema = Schema.Parser().parse(
            """
            {"type":"record","name":"Amounts","fields":[
                {"name":"units","type":{"type":"bytes","logicalType":"decimal","precision":100,"scale":0}},
                {"name":"price","type":["null",{"type":"bytes","logicalType":"decimal","precision":10,"scale":2}],"default":null}
            ]}
            """.trimIndent()
        )
        val units = BigInteger("123456789012345678901234567890")

        val record = buildGenericRecord(decimalSchema) {
            set("units", units)
            set("price", BigDecimal("1.5"))
        }
        val roundTripped = record.serializeAndDeserializeWith(decimalSchema)

        assertThat(roundTripped.getBigInteger("units")).isEqualTo(units)
        assertThat(roundTripped.getBigDecimal("price")).isEqualTo(BigDecimal("1.50"))
    }
}
