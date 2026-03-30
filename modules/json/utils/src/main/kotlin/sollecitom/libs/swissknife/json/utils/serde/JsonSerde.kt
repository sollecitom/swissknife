package sollecitom.libs.swissknife.json.utils.serde

import sollecitom.libs.swissknife.json.utils.JsonSchema

/** Combined JSON serializer and deserializer for a given type. */
interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    /** A [JsonSerde] that also carries a [JsonSchema] for validation. */
    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: JsonSchema

        interface WithSubTypes<VALUE : Any> : SchemaAware<VALUE> {

            val types: Set<String>
        }
    }
}