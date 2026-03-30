package sollecitom.libs.swissknife.avro.serialization.utils

import sollecitom.libs.swissknife.serialization.domain.Serializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

/** Serializes domain values of type [VALUE] to Avro [GenericRecord]s. */
interface AvroSerializer<in VALUE : Any> : Serializer<VALUE, GenericRecord> {

    val schema: Schema
}

/** Encodes the given [record] to Avro binary format. */
fun <VALUE : Any> AvroSerializer<VALUE>.writeAsBytes(record: GenericRecord): ByteArray = AvroSerializationUtils.writeAsBytes(record)

/** Serializes a domain value all the way to Avro binary bytes. */
fun <VALUE : Any> AvroSerializer<VALUE>.serializeToBytes(value: VALUE): ByteArray = serialize(value).let(::writeAsBytes)

/** Builds a [GenericRecord] using this serializer's schema and a builder DSL. */
fun <VALUE : Any> AvroSerializer<VALUE>.buildRecord(customize: AvroRecordBuilder.() -> Unit): GenericRecord = AvroRecordBuilder(schema).apply(customize).build()