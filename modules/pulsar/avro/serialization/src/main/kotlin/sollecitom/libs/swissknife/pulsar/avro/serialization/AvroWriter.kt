package sollecitom.libs.swissknife.pulsar.avro.serialization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaWriter

internal class AvroWriter<VALUE : Any>(originalAvroSchema: Schema, private val serialize: (VALUE) -> GenericRecord) : SchemaWriter<VALUE> {

    constructor(serializer: AvroSerializer<VALUE>) : this(serializer.schema, serializer::serialize)

    private val writer = GenericAvroWriter(originalAvroSchema)

    override fun write(message: VALUE): ByteArray {

        val record = serialize(message)
        return writer.write(record)
    }
}