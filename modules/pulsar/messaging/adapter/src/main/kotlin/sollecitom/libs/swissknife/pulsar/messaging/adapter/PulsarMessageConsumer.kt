package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.kotlin.extensions.async.await
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol.toReceivedMessage
import kotlinx.coroutines.future.await
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder

internal class PulsarMessageConsumer<out VALUE>(override val topics: Set<Topic>, initializeConsumer: (Set<Topic>) -> ConsumerBuilder<VALUE>) : MessageConsumer<VALUE> {

    private val consumer by lazy { initializeConsumer(topics).subscribe() }
    override val name by lazy { Name(consumer.consumerName) }
    override val subscriptionName by lazy { Name(consumer.subscription) }

    override suspend fun receive() = consumer.nextReceivedMessage()

    override suspend fun start() {
        consumer.resume()
        check(consumer.isConnected) { "Pulsar consumer is not connected" }
    }

    override suspend fun stop() = consumer.closeAsync().await()

    override fun close() = stopBlocking()

    private suspend fun <VALUE> Consumer<VALUE>.nextReceivedMessage(): ReceivedMessage<VALUE> = receiveAsync().await().toReceivedMessage()
}