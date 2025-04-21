package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema

private class PulsarMessageProducer<in VALUE>(override val name: Name, override val topic: Topic, private val producerBuilder: ProducerBuilder<VALUE>) : MessageProducer<VALUE> {

    private val producer by lazy { producerBuilder.producerName(name.value).topic(topic).create() }

    override suspend fun produce(message: Message<VALUE>) = producer.produce(message)

    override suspend fun start() {
        check(producer.isConnected) { "Pulsar producer not connected" }
    }

    override suspend fun stop() = producer.close()

    override fun close() = stopBlocking()
}

fun <VALUE> PulsarClient.newMessageProducer(name: Name, topic: Topic, producer: ProducerBuilder<VALUE>): MessageProducer<VALUE> = PulsarMessageProducer(name, topic, producer)

fun <VALUE> PulsarClient.newMessageProducer(name: Name, topic: Topic, schema: Schema<VALUE>, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { defaultProducerCustomization() }) = newMessageProducer(name, topic, newProducer(schema).customize())