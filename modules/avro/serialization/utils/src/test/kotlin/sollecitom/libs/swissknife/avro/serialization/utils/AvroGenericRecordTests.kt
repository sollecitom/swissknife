package sollecitom.libs.swissknife.avro.serialization.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
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

        val roundTripped = AvroSerializationUtils.readFromBytes(AvroSerializationUtils.writeAsBytes(record), schema)

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
}
