package sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import org.apache.pulsar.client.api.MessageIdAdv

internal data class PulsarMessageId(override val topic: Topic, private val delegate: MessageIdAdv) : Message.Id {

    override val stringRepresentation = "(ledger: ${delegate.ledgerId}, entry: ${delegate.entryId}, partition: ${delegate.partitionIndex}, batch: ${delegate.batchIndex})"
    override val partition: Topic.Partition? = delegate.partitionIndex.takeUnless { it == -1 }?.let(Topic::Partition)

    internal fun toByteArray(): ByteArray = delegate.toByteArray()

    override fun compareTo(other: Message.Id): Int {

        if (other !is PulsarMessageId) error("Cannot compare a PulsarMessageId with a ${other::class.java.name}")
        return delegate.compareTo(other.delegate)
    }

    override fun toString() = stringRepresentation
}

internal fun MessageIdAdv.adapted(topic: Topic): Message.Id = PulsarMessageId(topic, this)