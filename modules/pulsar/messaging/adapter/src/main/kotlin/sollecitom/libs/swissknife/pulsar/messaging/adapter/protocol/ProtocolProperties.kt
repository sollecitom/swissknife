package sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol

internal object ProtocolProperties {

    private const val PROTOCOL_PROPERTY_PREFIX = "PROTOCOL"

    fun removeFrom(properties: Map<String, String>): Map<String, String> = properties.filterKeys { !it.startsWith(PROTOCOL_PROPERTY_PREFIX) }

    fun contextualize(propertyKey: String): String = "$PROTOCOL_PROPERTY_PREFIX-$propertyKey"
}