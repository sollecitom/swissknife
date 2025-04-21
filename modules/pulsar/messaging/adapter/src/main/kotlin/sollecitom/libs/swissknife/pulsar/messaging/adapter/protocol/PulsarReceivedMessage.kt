package sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.kotlin.extensions.async.await
import sollecitom.libs.swissknife.kotlin.extensions.concurrency.VirtualThreads
import sollecitom.libs.swissknife.kotlin.extensions.text.removeFromLast
import sollecitom.libs.swissknife.messaging.domain.message.Message.Id
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageIdAdv as PulsarMessageIdAdv

internal class PulsarReceivedMessage<out VALUE>(private val delegate: Message<VALUE>, private val consumer: Consumer<VALUE>) : ReceivedMessage<VALUE> {

    override val id: Id by lazy { (delegate.messageId as PulsarMessageIdAdv).adapted(topic = delegate.topicName.withoutPartitionId().let(Topic.Companion::parse)) }
    override val key: String get() = delegate.key
    override val rawData: ByteArray get() = delegate.data
    override val value: VALUE get() = delegate.value
    override val publishedAt: Instant by lazy { Instant.fromEpochMilliseconds(delegate.publishTime) }
    override val properties by lazy { ProtocolProperties.removeFrom(delegate.properties) }
    override val context by lazy { MessageContextPropertiesSerde.deserialize(delegate.properties) }
    override val producerName by lazy { Name(delegate.producerName) }

    override suspend fun acknowledge() = consumer.acknowledgeAsync(delegate).await()
    override suspend fun acknowledgeAsFailed() = withContext(Dispatchers.VirtualThreads) { consumer.negativeAcknowledge(delegate) }

    private fun String.withoutPartitionId(): String = removeFromLast(PARTITION_TOPIC_PREFIX)

    companion object {

        private const val PARTITION_TOPIC_PREFIX = "-partition"
    }
}

context(consumer: Consumer<VALUE>)
internal fun <VALUE> Message<VALUE>.toReceivedMessage(): ReceivedMessage<VALUE> = PulsarReceivedMessage(this, consumer)