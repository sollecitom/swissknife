package sollecitom.libs.swissknife.pulsar.avro.serialization

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.schema.SchemaDefinition
import org.apache.pulsar.client.impl.schema.AvroSchema

fun <VALUE : Any> AvroSerde<VALUE>.asPulsarSchema(): AvroSchema<VALUE> = PulsarSchemas.forSerde(this)

private object PulsarSchemas {

    fun <VALUE : Any> forSerde(serde: AvroSerde<VALUE>): AvroSchema<VALUE> = createSchema(serde)

    private fun <VALUE : Any> createSchema(serde: AvroSerde<VALUE>): AvroSchema<VALUE> {

        val writer = AvroWriter(serde)
        val reader = AvroReader(serde)
        val definition = SchemaDefinition.builder<VALUE>().withSchemaWriter(writer).withSchemaReader(reader).withJsonDef(serde.schema.toString()).build()
        return Schema.AVRO(definition) as AvroSchema<VALUE>
    }
}