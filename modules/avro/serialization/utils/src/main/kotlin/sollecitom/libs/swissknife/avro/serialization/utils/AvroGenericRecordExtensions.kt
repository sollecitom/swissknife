@file:Suppress("UNCHECKED_CAST")

package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

/** Type-safe extensions for extracting fields from Avro [GenericRecord]s. Each pair provides a nullable and a non-nullable variant that throws on missing fields. */

fun GenericRecord.getOrNull(key: String): Any? = get(key)

fun GenericRecord.getStringOrNull(key: String): String? = get(key)?.asString()
fun GenericRecord.getString(key: String) = getStringOrNull(key) ?: missingField(key)

fun GenericRecord.getIntOrNull(key: String): Int? = get(key)?.let { it as Int }
fun GenericRecord.getInt(key: String) = getIntOrNull(key) ?: missingField(key)

fun GenericRecord.getBigIntegerOrNull(key: String): BigInteger? = get(key)?.let { it as BigInteger }
fun GenericRecord.getBigInteger(key: String) = getBigIntegerOrNull(key) ?: missingField(key)

fun GenericRecord.getBooleanOrNull(key: String): Boolean? = get(key)?.let { it as Boolean }
fun GenericRecord.getBoolean(key: String) = getBooleanOrNull(key) ?: missingField(key)

fun GenericRecord.getBigDecimalOrNull(key: String): BigDecimal? = get(key)?.let { it as BigDecimal }
fun GenericRecord.getBigDecimal(key: String) = getBigDecimalOrNull(key) ?: missingField(key)

fun GenericRecord.getLongOrNull(key: String): Long? = get(key)?.let { it as Long }
fun GenericRecord.getLong(key: String) = getLongOrNull(key) ?: missingField(key)

fun GenericRecord.getStringListOrNull(key: String): List<String>? = get(key)?.let { it as List<Any> }?.map { it.asString() }
fun GenericRecord.getStringList(key: String) = getStringListOrNull(key) ?: missingField(key)

fun GenericRecord.getDoubleOrNull(key: String): Double? = get(key)?.let { it as Double }
fun GenericRecord.getDouble(key: String) = getDoubleOrNull(key) ?: missingField(key)

fun GenericRecord.getInstantOrNull(key: String): Instant? = getStringOrNull(key)?.let(Instant::parse)
fun GenericRecord.getInstant(key: String) = getInstantOrNull(key) ?: missingField(key)

fun GenericRecord.getEnumOrNull(key: String): String? = get(key)?.let { (get(key) as GenericData.EnumSymbol).toString() }
fun GenericRecord.getEnum(key: String) = getEnumOrNull(key) ?: missingField(key)

fun GenericRecord.getRecordOrNull(key: String): GenericRecord? = get(key)?.let { get(key) as GenericRecord }
fun GenericRecord.getRecord(key: String) = getRecordOrNull(key) ?: missingField(key)

fun GenericRecord.getRecordListOrNull(key: String): List<GenericRecord>? = get(key)?.let { it as List<GenericRecord> }
fun GenericRecord.getRecordList(key: String) = getRecordListOrNull(key) ?: missingField(key)

fun GenericRecord.getHexStringAsByteArrayOrNull(key: String): ByteArray? = getStringOrNull(key)?.let(HexFormat.of()::parseHex)
fun GenericRecord.getHexStringAsByteArray(key: String): ByteArray = getHexStringAsByteArrayOrNull(key) ?: missingField(key)

/** Extracts and deserializes a record from an envelope-style union (string type discriminator + nested record). */
fun <T> GenericRecord.getRecordFromUnion(deserialize: (type: String, record: GenericRecord) -> T): T {

    val envelopeType = getString(EnvelopeFields.ENVELOPE_TYPE)
    val envelope = getRecord(EnvelopeFields.ENVELOPE)

    return deserialize(envelopeType, envelope)
}

/** Like [getRecordFromUnion] but reads the type discriminator as an Avro enum instead of a string. */
fun <T> GenericRecord.getRecordFromUnionWithEnumType(deserialize: (type: String, record: GenericRecord) -> T): T {

    val envelopeType = getEnum(EnvelopeFields.ENVELOPE_TYPE)
    val envelope = getRecord(EnvelopeFields.ENVELOPE)

    return deserialize(envelopeType, envelope)
}

/** Deserializes this [GenericRecord] using the given [deserializer]. */
fun <T : Any> GenericRecord.deserializeWith(deserializer: AvroDeserializer<T>): T = deserializer.deserialize(this)

fun <T : Any> GenericRecord.getValueOrNull(key: String, deserializer: AvroDeserializer<T>): T? = getRecordOrNull(key)?.deserializeWith(deserializer)
fun <T : Any> GenericRecord.getValue(key: String, deserializer: AvroDeserializer<T>): T = getValueOrNull(key, deserializer) ?: missingField(key)

fun <T : Any> GenericRecord.getValuesOrNull(key: String, deserializer: AvroDeserializer<T>): List<T>? = getRecordListOrNull(key)?.map(deserializer::deserialize)
fun <T : Any> GenericRecord.getValues(key: String, deserializer: AvroDeserializer<T>): List<T> = getValuesOrNull(key, deserializer) ?: missingField(key)

private fun missingField(key: String): Nothing = error("Required Avro field '$key' is missing or null")
