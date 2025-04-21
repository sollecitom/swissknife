package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant

fun GenericRecord.copy(schema: Schema = getSchema(), customise: AvroRecordBuilder.() -> Unit): GenericRecord = buildGenericRecord(this, schema, customise)

interface AvroRecordBuilder {

    val schema: Schema

    fun build(): GenericRecord

    fun set(fieldName: String, value: String?): AvroRecordBuilder
    fun set(fieldName: String, value: Double?): AvroRecordBuilder
    fun set(fieldName: String, value: Int?): AvroRecordBuilder
    fun set(fieldName: String, value: BigInteger?): AvroRecordBuilder
    fun set(fieldName: String, value: BigDecimal?): AvroRecordBuilder
    fun set(fieldName: String, value: Long?): AvroRecordBuilder
    fun set(fieldName: String, value: Boolean?): AvroRecordBuilder
    fun set(fieldName: String, value: GenericRecord?): AvroRecordBuilder
    fun set(fieldName: String, value: Instant?): AvroRecordBuilder
    fun set(fieldName: String, value: Map<String, String>?): AvroRecordBuilder
    fun setByteArrayAsHexString(fieldName: String, bytes: ByteArray?): AvroRecordBuilder
    fun unset(fieldName: String): AvroRecordBuilder

    fun setStrings(fieldName: String, value: List<String>?): AvroRecordBuilder
    fun setDoubles(fieldName: String, value: List<Double>?): AvroRecordBuilder
    fun setInts(fieldName: String, value: List<Int>?): AvroRecordBuilder
    fun setLongs(fieldName: String, value: List<Long>?): AvroRecordBuilder
    fun setBooleans(fieldName: String, value: List<Boolean>?): AvroRecordBuilder
    fun setEnum(fieldName: String, value: Any?): AvroRecordBuilder
    fun setRecordInUnion(unionType: String, record: GenericRecord?): AvroRecordBuilder
    fun setRecordInUnionWithEnumType(unionType: String, record: GenericRecord?): AvroRecordBuilder
    fun setRecordInUnion(unionType: String, customizeRecord: AvroRecordBuilder.() -> Unit): AvroRecordBuilder
    fun setInstants(fieldName: String, value: List<Instant>?): AvroRecordBuilder

    fun setRecords(fieldName: String, value: List<GenericRecord>?): AvroRecordBuilder

    companion object
}

operator fun AvroRecordBuilder.Companion.invoke(schema: Schema): AvroRecordBuilder = AvroRecordBuilderImpl(schema)
operator fun AvroRecordBuilder.Companion.invoke(recordToCopy: GenericRecord, schema: Schema = recordToCopy.schema): AvroRecordBuilder = AvroRecordBuilderImpl(schema, recordToCopy)

inline fun buildGenericRecord(schema: Schema, customise: AvroRecordBuilder.() -> Unit): GenericRecord = AvroRecordBuilder(schema).also(customise).build()
inline fun buildGenericRecord(recordToCopy: GenericRecord, schema: Schema = recordToCopy.schema, customise: AvroRecordBuilder.() -> Unit): GenericRecord = AvroRecordBuilder(recordToCopy, schema).also(customise).build()



