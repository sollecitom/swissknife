package sollecitom.libs.swissknife.web.api.utils

import org.json.JSONObject

/** Utility for building standard JSON error response bodies. */
object Error {

    /** Creates a JSON object with a single "message" field. */
    fun withMessage(message: String): JSONObject = JSONObject().apply {
        put(Fields.MESSAGE, message)
    }

    private object Fields {
        const val MESSAGE = "message"
    }
}