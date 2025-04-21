package sollecitom.libs.swissknife.json.utils.serde

import sollecitom.libs.swissknife.json.utils.getArrayOrNull
import sollecitom.libs.swissknife.json.utils.getJSONObjectOrNull
import sollecitom.libs.swissknife.json.utils.getRequiredJSONObject
import org.json.JSONArray
import org.json.JSONObject

fun <VALUE : Any> JSONObject.getValueOrNull(key: String, deserializer: JsonDeserializer<VALUE>): VALUE? = getJSONObjectOrNull(key)?.let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.getValue(key: String, deserializer: JsonDeserializer<VALUE>): VALUE = getRequiredJSONObject(key).let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.setValue(key: String, value: VALUE, serializer: JsonSerializer<VALUE>): JSONObject = put(key, serializer.serialize(value))

fun <VALUE : Any> JSONObject.setValues(key: String, values: Iterable<VALUE>, serializer: JsonSerializer<VALUE>): JSONObject = setValuesOrNull(key, values, serializer)

fun <VALUE : Any> JSONObject.setValuesOrNull(key: String, values: Iterable<VALUE>?, serializer: JsonSerializer<VALUE>): JSONObject = put(key, values?.map(serializer::serialize)?.fold(JSONArray(), JSONArray::put))

fun <VALUE : Any> JSONObject.getValues(key: String, deserializer: JsonDeserializer<VALUE>): List<VALUE> = getValuesOrNull(key, deserializer)!!

fun <VALUE : Any> JSONObject.getValuesOrNull(key: String, deserializer: JsonDeserializer<VALUE>): List<VALUE>? = getArrayOrNull(key)?.map { it as JSONObject }?.map(deserializer::deserialize)

fun <VALUE : Any> JSONObject.setValueOrNull(key: String, value: VALUE?, serializer: JsonSerializer<VALUE>): JSONObject = put(key, value?.let { serializer.serialize(it) })

fun JSONObject.putAll(key: String, values: Collection<*>?): JSONObject {

    when {
        values != null -> put(key, values)
        else -> remove(key)
    }
    return this
}