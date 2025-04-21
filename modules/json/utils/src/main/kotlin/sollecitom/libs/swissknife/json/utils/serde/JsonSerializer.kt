package sollecitom.libs.swissknife.json.utils.serde

import sollecitom.libs.swissknife.serialization.domain.Serializer
import org.json.JSONObject

typealias JsonSerializer<VALUE> = Serializer<VALUE, JSONObject>