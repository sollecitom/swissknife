package sollecitom.libs.swissknife.messaging.domain.message.properties

interface MessagePropertyNames {

    val forEvents: ForEvents

    interface ForEvents {

        val type: String
    }
}