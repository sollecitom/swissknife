package sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.MessageIdAdv
import org.apache.pulsar.shade.org.apache.commons.codec.binary.Hex

internal object MessageIdStringSerde {

    private const val TOPIC_PREFIX = "-:TOPIC:"
    private const val ID_PREFIX = "-:ID:"

    fun serialize(id: Message.Id): String = "$TOPIC_PREFIX${id.topic.fullName.value}$ID_PREFIX${(id as PulsarMessageId).toByteArray().serializeToString()}"

    fun deserialize(value: String): Message.Id {

        val idHexString = value.substringAfterLast(ID_PREFIX)
        val topicFullName = value.replace(TOPIC_PREFIX, "").substringBeforeLast(ID_PREFIX)
        val topic = Topic.parse(topicFullName)
        val id = MessageId.fromByteArrayWithTopic(idHexString.deserializeToByteArray(), topicFullName)
        return (id as MessageIdAdv).adapted(topic)
    }

    private fun ByteArray.serializeToString() = Hex.encodeHexString(this)

    private fun String.deserializeToByteArray() = Hex.decodeHex(this)
}