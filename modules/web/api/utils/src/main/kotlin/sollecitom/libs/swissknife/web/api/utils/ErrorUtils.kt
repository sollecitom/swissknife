package sollecitom.libs.swissknife.web.api.utils

import org.json.JSONObject

object Error {

    fun withMessage(message: String): JSONObject = JSONObject().apply {
        put(Fields.MESSAGE, message)
    }

    private object Fields {
        const val MESSAGE = "message"
    }
}