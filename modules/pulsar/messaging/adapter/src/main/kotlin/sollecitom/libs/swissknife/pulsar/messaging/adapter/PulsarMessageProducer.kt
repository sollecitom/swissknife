package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema

private class PulsarMessageProducer<in VALUE>(override val name: Name, override val topic: Topic, private val producerBuilder: ProducerBuilder<VALUE>) : MessageProducer<VALUE> {

    private val creation = Mutex()
    @Volatile private var producer: Producer<VALUE>? = null

    override suspend fun produce(message: Message<VALUE>) = producer().produce(message)

    override suspend fun start() {
        check(producer().isConnected) { "Pulsar producer not connected" }
    }

    override suspend fun stop() {
        creation.withLock { producer?.closeAsync()?.await() }
    }

    override fun close() = stopBlocking()

    private suspend fun producer(): Producer<VALUE> = producer ?: creation.withLock { producer ?: producerBuilder.producerName(name.value).topic(topic).createAsync().await().also { producer = it } }
}

fun <VALUE> PulsarClient.newMessageProducer(name: Name, topic: Topic, producer: ProducerBuilder<VALUE>): MessageProducer<VALUE> = PulsarMessageProducer(name, topic, producer)

fun <VALUE> PulsarClient.newMessageProducer(name: Name, topic: Topic, schema: Schema<VALUE>, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { defaultProducerCustomization() }) = newMessageProducer(name, topic, newProducer(schema).customize())
