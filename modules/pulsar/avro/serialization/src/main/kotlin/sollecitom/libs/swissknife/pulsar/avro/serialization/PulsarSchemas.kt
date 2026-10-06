package sollecitom.libs.swissknife.pulsar.avro.serialization

import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SchemaSerializationException
import org.apache.pulsar.client.api.schema.SchemaInfoProvider
import org.apache.pulsar.common.schema.SchemaInfo
import org.apache.pulsar.common.schema.SchemaType
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import java.io.ByteArrayOutputStream
import java.util.concurrent.ConcurrentHashMap
import org.apache.avro.Schema as AvroSchema

/** A Pulsar [Schema] for this serde. Messages are decoded with the schema version they were written with, resolved against this serde's schema. */
fun <VALUE : Any> AvroSerde<VALUE>.asPulsarSchema(): Schema<VALUE> = AvroPulsarSchema(this)

private class AvroPulsarSchema<VALUE : Any>(private val serde: AvroSerde<VALUE>) : Schema<VALUE> {

    private val schemaInfo = SchemaInfo.builder().name(serde.schema.fullName).type(SchemaType.AVRO).schema(serde.schema.toString().toByteArray()).properties(emptyMap()).build()
    private val writer = GenericDatumWriter<GenericRecord>(serde.schema)
    private val currentSchemaReader = GenericDatumReader<GenericRecord>(serde.schema)
    private val readersByWriterSchemaVersion = ConcurrentHashMap<String, GenericDatumReader<GenericRecord>>()
    @Volatile
    private var schemaInfoProvider: SchemaInfoProvider? = null

    override fun encode(message: VALUE): ByteArray = serializationStep {
        val bytes = ByteArrayOutputStream()
        val encoder = EncoderFactory.get().binaryEncoder(bytes, null)
        writer.write(serde.serialize(message), encoder)
        encoder.flush()
        bytes.toByteArray()
    }

    override fun decode(bytes: ByteArray): VALUE = decode(bytes, null)

    override fun decode(bytes: ByteArray, schemaVersion: ByteArray?): VALUE = serializationStep {
        val record = readerFor(schemaVersion).read(null, DecoderFactory.get().binaryDecoder(bytes, null))
        serde.deserialize(record)
    }

    override fun getSchemaInfo() = schemaInfo

    override fun supportSchemaVersioning() = true

    override fun setSchemaInfoProvider(schemaInfoProvider: SchemaInfoProvider) {
        this.schemaInfoProvider = schemaInfoProvider
    }

    override fun clone(): Schema<VALUE> = AvroPulsarSchema(serde)

    private fun readerFor(schemaVersion: ByteArray?): GenericDatumReader<GenericRecord> {

        val provider = schemaInfoProvider ?: return currentSchemaReader
        if (schemaVersion == null) return currentSchemaReader
        return readersByWriterSchemaVersion.computeIfAbsent(schemaVersion.toHexString()) {
            val writerSchemaInfo = provider.getSchemaByVersion(schemaVersion).get() ?: throw SchemaSerializationException("No schema found for version ${schemaVersion.toHexString()}")
            val writerSchema = AvroSchema.Parser().parse(writerSchemaInfo.schemaDefinition)
            GenericDatumReader(writerSchema, serde.schema)
        }
    }

    private fun <RESULT> serializationStep(step: () -> RESULT): RESULT = try {
        step()
    } catch (error: SchemaSerializationException) {
        throw error
    } catch (error: Exception) {
        throw SchemaSerializationException(error)
    }
}
