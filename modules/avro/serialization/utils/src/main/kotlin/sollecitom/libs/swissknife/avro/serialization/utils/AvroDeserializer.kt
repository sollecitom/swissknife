package sollecitom.libs.swissknife.avro.serialization.utils

import sollecitom.libs.swissknife.serialization.domain.Deserializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

interface AvroDeserializer<out VALUE : Any> : Deserializer<GenericRecord, VALUE> {

    val schema: Schema
}

fun <VALUE : Any> AvroDeserializer<VALUE>.deserializeFromBytes(bytes: ByteArray): VALUE = readFromBytes(bytes).let(::deserialize)

fun <VALUE : Any> AvroDeserializer<VALUE>.readFromBytes(bytes: ByteArray): GenericRecord = AvroSerializationUtils.readFromBytes(bytes, schema)