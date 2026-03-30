package sollecitom.libs.swissknife.http4k.utils.lens

import sollecitom.libs.swissknife.json.utils.serde.JsonDeserializer
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.BodyLensSpec
import org.http4k.lens.ContentNegotiation
import org.http4k.lens.string
import org.json.JSONArray
import org.json.JSONObject

/** Creates a body lens spec for JSON objects with `application/json` content type. */
fun Body.Companion.jsonObject(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.StrictNoDirective): BiDiBodyLensSpec<JSONObject> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONObject, JSONObject::toString)

/** Creates a body lens spec for JSON arrays with `application/json` content type. */
fun Body.Companion.jsonArray(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.StrictNoDirective): BiDiBodyLensSpec<JSONArray> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONArray, JSONArray::toString)

/** Maps a JSON object body lens to a domain type using a [JsonSerde]. */
fun <VALUE : Any> BiDiBodyLensSpec<JSONObject>.map(serde: JsonSerde<VALUE>): BiDiBodyLensSpec<VALUE> = map(serde::deserialize, serde::serialize)

/** Maps a JSON object body lens to a domain type using a [JsonDeserializer] (read-only). */
fun <VALUE : Any> BodyLensSpec<JSONObject>.map(deserializer: JsonDeserializer<VALUE>): BodyLensSpec<VALUE> = map(deserializer::deserialize)