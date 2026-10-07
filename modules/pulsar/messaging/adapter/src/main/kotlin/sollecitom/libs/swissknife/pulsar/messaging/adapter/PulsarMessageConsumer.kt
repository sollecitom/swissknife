package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol.toReceivedMessage
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder

internal class PulsarMessageConsumer<VALUE>(override val topics: Set<Topic>, private val initializeConsumer: (Set<Topic>) -> ConsumerBuilder<VALUE>) : MessageConsumer<VALUE> {

    private val subscription = Mutex()
    @Volatile private var consumer: Consumer<VALUE>? = null

    override val name get() = Name(subscribed().consumerName)
    override val subscriptionName get() = Name(subscribed().subscription)

    override suspend fun receive(): ReceivedMessage<VALUE> = with(consumer()) { receiveAsync().await().toReceivedMessage() }

    override suspend fun start() {
        val consumer = consumer()
        consumer.resume()
        check(consumer.isConnected) { "Pulsar consumer is not connected" }
    }

    override suspend fun stop() {
        subscription.withLock { consumer?.closeAsync()?.await() }
    }

    override fun close() = stopBlocking()

    private suspend fun consumer(): Consumer<VALUE> = consumer ?: subscription.withLock { consumer ?: initializeConsumer(topics).subscribeAsync().await().also { consumer = it } }

    private fun subscribed(): Consumer<VALUE> = checkNotNull(consumer) { "The Pulsar consumer has not subscribed yet: start it first" }
}
