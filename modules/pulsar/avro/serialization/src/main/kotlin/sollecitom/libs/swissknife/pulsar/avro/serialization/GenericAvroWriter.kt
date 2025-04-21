package sollecitom.libs.swissknife.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.EncoderFactory
import org.apache.pulsar.client.api.SchemaSerializationException
import org.apache.pulsar.client.api.schema.SchemaWriter
import java.io.ByteArrayOutputStream

// Copied from Pulsar and lightly modified
internal class GenericAvroWriter(schema: Schema) : SchemaWriter<GenericRecord> {

    private val writer: GenericDatumWriter<GenericRecord> = GenericDatumWriter(schema)
    private val byteArrayOutputStream = ByteArrayOutputStream()
    private val encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null)

    @Synchronized
    override fun write(message: GenericRecord): ByteArray = try {
        writer.write(message, encoder)
        encoder.flush()
        byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        throw SchemaSerializationException(e)
    } finally {
        byteArrayOutputStream.reset()
    }
}