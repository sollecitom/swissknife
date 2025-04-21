package sollecitom.libs.swissknife.json.utils.serde

import sollecitom.libs.swissknife.json.utils.JsonSchema

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: JsonSchema

        interface WithSubTypes<VALUE : Any> : SchemaAware<VALUE> {

            val types: Set<String>
        }
    }
}