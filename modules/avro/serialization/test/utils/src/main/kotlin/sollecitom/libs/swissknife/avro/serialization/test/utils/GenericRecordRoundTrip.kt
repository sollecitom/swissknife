package sollecitom.libs.swissknife.avro.serialization.test.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerializationUtils

fun GenericRecord.serializeAndDeserializeWith(readerSchema: Schema): GenericRecord = AvroSerializationUtils.writeAsBytes(this).let { AvroSerializationUtils.readFromBytes(bytes = it, readerSchema = readerSchema, writerSchema = schema) }
