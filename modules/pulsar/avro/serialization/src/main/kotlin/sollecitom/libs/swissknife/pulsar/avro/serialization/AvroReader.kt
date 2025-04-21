package sollecitom.libs.swissknife.pulsar.avro.serialization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroDeserializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaReader
import java.io.InputStream

internal class AvroReader<VALUE : Any>(originalAvroSchema: Schema, private val useProvidedSchemaAsReaderSchema: Boolean = true, private val deserialize: (GenericRecord) -> VALUE) : SchemaReader<VALUE> {

    constructor(deserializer: AvroDeserializer<VALUE>, useProvidedSchemaAsReaderSchema: Boolean = true) : this(deserializer.schema, useProvidedSchemaAsReaderSchema, deserializer::deserialize)

    private val reader = MultiVersionGenericAvroReader(useProvidedSchemaAsReaderSchema, originalAvroSchema)

    override fun read(bytes: ByteArray, offset: Int, length: Int): VALUE {

        val record = reader.read(bytes, offset, length)
        return deserialize(record)
    }

    override fun read(inputStream: InputStream): VALUE {

        val record = reader.read(inputStream)
        return deserialize(record)
    }
}