package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.Conversions
import org.apache.avro.LogicalTypes
import org.apache.avro.Schema
import java.util.HexFormat
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.time.Instant

internal class AvroRecordBuilderImpl(schema: Schema) : GenericRecordBuilder(schema), AvroRecordBuilder {

    constructor(schema: Schema, other: GenericRecord) : this(schema) {

        deepCopyFieldsWithTheirSchemaFromRecord(other)
    }

    override val schema: Schema get() = super.schema()

    override fun set(fieldName: String, value: String?) = setValueAndReturnSelf(fieldName, value)

    override fun set(fieldName: String, value: Double?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun set(fieldName: String, value: Int?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun set(fieldName: String, value: BigInteger?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value?.let { decimalBytesOrNull(fieldName, it.toBigDecimal()) ?: it.toString() })
    override fun set(fieldName: String, value: BigDecimal?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value?.let { decimalBytesOrNull(fieldName, it) ?: it.toString() })
    override fun set(fieldName: String, value: Long?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun set(fieldName: String, value: Boolean?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun set(fieldName: String, value: GenericRecord?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun set(fieldName: String, value: Instant?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value?.toString())
    override fun set(fieldName: String, value: Map<String, String>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun setByteArrayAsHexString(fieldName: String, bytes: ByteArray?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, bytes?.let(HexFormat.of()::formatHex))

    override fun setStrings(fieldName: String, value: List<String>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun setDoubles(fieldName: String, value: List<Double>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun setInts(fieldName: String, value: List<Int>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun setLongs(fieldName: String, value: List<Long>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)
    override fun setBooleans(fieldName: String, value: List<Boolean>?): AvroRecordBuilder = setValueAndReturnSelf(fieldName, value)

    override fun unset(fieldName: String): AvroRecordBuilder {
        super.clear(fieldName)
        return this
    }

    override fun setEnum(fieldName: String, value: Any?): AvroRecordBuilder = if (value != null) {
        val fieldSchema = schema.getEnumFieldSchema(fieldName)
        val genericEnumSymbol = GenericData.EnumSymbol(fieldSchema, value)
        super.set(fieldName, genericEnumSymbol)
        this
    } else {
        unset(fieldName)
    }

    override fun setRecordInUnion(record: GenericRecord?): AvroRecordBuilder {

        super.set(ENVELOPE_FIELD, record)
        return this
    }

    override fun setRecordInUnion(branchName: String, customizeRecord: AvroRecordBuilder.() -> Unit): AvroRecordBuilder = setRecordInUnion(buildGenericRecord(envelopeBranchSchema(branchName), customizeRecord))

    override fun setInstants(fieldName: String, value: List<Instant>?): AvroRecordBuilder = value?.map(Instant::toString).ifNotNullOrUnset(fieldName, ::setStrings)

    override fun setRecords(fieldName: String, value: List<GenericRecord>?): AvroRecordBuilder = if (value != null) {
        val fieldSchema = schema.fields.single { it.name() == fieldName }.schema().arraySchema()
        set(fieldName, GenericData.Array(fieldSchema, value))
        this
    } else {
        unset(fieldName)
    }

    private fun envelopeBranchSchema(branchName: String): Schema = schema.getField(ENVELOPE_FIELD).schema().types.firstOrNull { it.name == branchName } ?: throw IllegalArgumentException("Union field '$ENVELOPE_FIELD' of ${schema.fullName} has no branch named '$branchName'")

    private fun Schema.getEnumFieldSchema(fieldName: String): Schema {

        val fieldSchema = getField(fieldName).schema()

        return when (fieldSchema.type) {
            Schema.Type.ENUM -> return fieldSchema
            Schema.Type.UNION -> return fieldSchema.types.first { it.type == Schema.Type.ENUM }
            else -> fieldSchema
        }
    }

    private fun decimalBytesOrNull(fieldName: String, value: BigDecimal): ByteBuffer? {

        val fieldSchema = schema.getField(fieldName).schema().nonNullBranch().takeIf { it.type == Schema.Type.BYTES } ?: return null
        val decimal = fieldSchema.logicalType as? LogicalTypes.Decimal ?: return null
        return Conversions.DecimalConversion().toBytes(value.setScale(decimal.scale), fieldSchema, decimal)
    }

    private fun Schema.arraySchema(): Schema = if (type == Schema.Type.UNION) types.first { it.type == Schema.Type.ARRAY } else this

    private fun setValueAndReturnSelf(fieldName: String, value: Any?): AvroRecordBuilder {

        super.set(fieldName, value)
        return this
    }

    private fun deepCopyFieldsWithTheirSchemaFromRecord(other: GenericRecord) {

        other.schema.fields.forEach { field ->
            val value = other[field.pos()]
            if (isValidValue(field, value)) {
                set(field, data().deepCopy(field.schema(), value))
            }
        }
    }

    private fun <T : Any> T?.ifNotNullOrUnset(fieldName: String, action: (String, T) -> AvroRecordBuilder): AvroRecordBuilder {

        return if (this != null) action(fieldName, this) else unset(fieldName)
    }
}
