package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import org.json.JSONArray
import org.json.JSONObject

internal sealed class JsonValue(open val value: Any) {

    class Object(override val value: JSONObject) : JsonValue(value)
    class Array(override val value: JSONArray) : JsonValue(value)
}