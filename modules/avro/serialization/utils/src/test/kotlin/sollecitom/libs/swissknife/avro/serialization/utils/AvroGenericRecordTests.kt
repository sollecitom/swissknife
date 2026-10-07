package sollecitom.libs.swissknife.avro.serialization.utils

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import sollecitom.libs.swissknife.avro.serialization.test.utils.serializeAndDeserializeWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

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
    fun `reading with an evolved schema fills a newly added field with its default`() {

        val writerSchema = Schema.Parser().parse("""{"type":"record","name":"Person","fields":[{"name":"name","type":"string"}]}""")
        val readerSchema = Schema.Parser().parse("""{"type":"record","name":"Person","fields":[{"name":"name","type":"string"},{"name":"age","type":"int","default":7}]}""")
        val record = buildGenericRecord(writerSchema) { set("name", "Bruce") }

        val evolved = record.serializeAndDeserializeWith(readerSchema)

        assertThat(evolved.getString("name")).isEqualTo("Bruce")
        assertThat(evolved.getInt("age")).isEqualTo(7)
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

    @Test
    fun `reading a big number from a bytes field that is not a decimal fails`() {

        val bytesSchema = Schema.Parser().parse("""{"type":"record","name":"Blob","fields":[{"name":"blob","type":"bytes"}]}""")
        val record = GenericData.Record(bytesSchema).apply { put("blob", ByteBuffer.wrap(byteArrayOf(1))) }

        val result = runCatching { record.getBigInteger("blob") }

        assertThat(result).failedThrowing<IllegalArgumentException>().hasMessage("Field 'blob' of Blob holds bytes but is not a decimal")
    }

    @Test
    fun `setting a big number on a field the schema does not declare fails`() {

        val result = runCatching { buildGenericRecord(schema) { set("missing", BigInteger("1")) } }

        assertThat(result).failedThrowing<IllegalArgumentException>().hasMessage("Not a valid schema field: missing")
    }
}
