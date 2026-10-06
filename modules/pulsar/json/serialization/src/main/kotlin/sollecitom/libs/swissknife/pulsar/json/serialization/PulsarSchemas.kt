package sollecitom.libs.swissknife.pulsar.json.serialization

import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SchemaSerializationException
import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde

/** A Pulsar [Schema] for this serde, carrying the JSON as a UTF-8 string. */
fun <VALUE : Any> JsonSerde.SchemaAware<VALUE>.asPulsarSchema(): Schema<VALUE> = JsonPulsarSchema(this)

private class JsonPulsarSchema<VALUE : Any>(private val serde: JsonSerde.SchemaAware<VALUE>) : Schema<VALUE> {

    override fun encode(message: VALUE): ByteArray = serde.serialize(message).toString().encodeToByteArray()

    override fun decode(bytes: ByteArray): VALUE = try {
        serde.deserialize(JSONObject(bytes.decodeToString()))
    } catch (error: Exception) {
        throw SchemaSerializationException(error)
    }

    override fun getSchemaInfo() = Schema.STRING.schemaInfo

    override fun clone(): Schema<VALUE> = this
}
