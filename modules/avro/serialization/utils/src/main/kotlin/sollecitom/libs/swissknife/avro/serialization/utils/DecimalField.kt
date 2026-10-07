package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.Conversions
import org.apache.avro.LogicalTypes
import org.apache.avro.Schema
import java.math.BigDecimal
import java.nio.ByteBuffer

internal class DecimalField(private val schema: Schema, private val decimal: LogicalTypes.Decimal) {

    fun encode(value: BigDecimal): ByteBuffer = conversion.toBytes(value.setScale(decimal.scale), schema, decimal)

    fun decode(bytes: ByteBuffer): BigDecimal = conversion.fromBytes(bytes.duplicate(), schema, decimal)

    private companion object {
        val conversion = Conversions.DecimalConversion()
    }
}

internal fun Schema.decimalFieldOrNull(fieldName: String): DecimalField? {

    val bytesSchema = requiredField(fieldName).schema().bytesBranchOrNull() ?: return null
    val decimal = bytesSchema.logicalType as? LogicalTypes.Decimal ?: return null
    return DecimalField(bytesSchema, decimal)
}

private fun Schema.requiredField(name: String): Schema.Field = getField(name) ?: throw IllegalArgumentException("Not a valid schema field: $name")

private fun Schema.bytesBranchOrNull(): Schema? = if (type == Schema.Type.UNION) types.singleOrNull { it.type == Schema.Type.BYTES } else takeIf { it.type == Schema.Type.BYTES }
