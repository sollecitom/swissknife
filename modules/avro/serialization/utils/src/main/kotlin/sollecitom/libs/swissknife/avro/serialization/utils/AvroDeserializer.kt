package sollecitom.libs.swissknife.avro.serialization.utils

import sollecitom.libs.swissknife.serialization.domain.Deserializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

/** Deserializes Avro [GenericRecord]s to domain values of type [VALUE]. */
interface AvroDeserializer<out VALUE : Any> : Deserializer<GenericRecord, VALUE> {

    val schema: Schema
}

/** Deserializes a domain value from Avro binary bytes. */
fun <VALUE : Any> AvroDeserializer<VALUE>.deserializeFromBytes(bytes: ByteArray): VALUE = readFromBytes(bytes).let(::deserialize)

/** Decodes Avro binary bytes into a [GenericRecord] using this deserializer's schema. */
fun <VALUE : Any> AvroDeserializer<VALUE>.readFromBytes(bytes: ByteArray): GenericRecord = AvroSerializationUtils.readFromBytes(bytes, schema)