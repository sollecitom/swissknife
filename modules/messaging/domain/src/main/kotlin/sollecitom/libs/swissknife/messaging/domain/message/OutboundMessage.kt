package sollecitom.libs.swissknife.messaging.domain.message

data class OutboundMessage<out VALUE>(override val key: String, override val value: VALUE, override val properties: Map<String, String>, override val context: Message.Context) : Message<VALUE> {

    override val rawData: ByteArray get() = error("Outbound message data isn't backed by a byte array")
}