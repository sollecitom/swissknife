package sollecitom.libs.swissknife.json.utils.serde

import sollecitom.libs.swissknife.serialization.domain.Deserializer
import org.json.JSONObject

typealias JsonDeserializer<VALUE> = Deserializer<JSONObject, VALUE>