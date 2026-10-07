package sollecitom.libs.swissknife.avro.serialization.test.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerializationUtils

fun GenericRecord.serializeAndDeserializeWith(schema: Schema): GenericRecord = AvroSerializationUtils.writeAsBytes(this).let { AvroSerializationUtils.readFromBytes(bytes = it, schema = schema, writerSchema = this.schema) }
