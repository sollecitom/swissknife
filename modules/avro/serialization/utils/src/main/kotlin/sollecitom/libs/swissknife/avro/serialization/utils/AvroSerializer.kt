package sollecitom.libs.swissknife.avro.serialization.utils

import sollecitom.libs.swissknife.serialization.domain.Serializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

interface AvroSerializer<in VALUE : Any> : Serializer<VALUE, GenericRecord> {

    val schema: Schema
}

fun <VALUE : Any> AvroSerializer<VALUE>.writeAsBytes(record: GenericRecord): ByteArray = AvroSerializationUtils.writeAsBytes(record)

fun <VALUE : Any> AvroSerializer<VALUE>.serializeToBytes(value: VALUE): ByteArray = serialize(value).let(::writeAsBytes)

fun <VALUE : Any> AvroSerializer<VALUE>.buildRecord(customize: AvroRecordBuilder.() -> Unit): GenericRecord = AvroRecordBuilder(schema).apply(customize).build()