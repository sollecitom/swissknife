package sollecitom.libs.swissknife.pulsar.messaing.test.utils.avro

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.Messages
import sollecitom.libs.swissknife.messaging.domain.message.MessagesImplementation
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultConsumerCustomization
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient

context(_: RandomGenerator)
fun <EVENT : Event> PulsarClient.messages(serde: AvroSerde<EVENT>, topic: Topic, messageConverter: MessageConverter<EVENT>, groupName: Name = Name.random(), nodeName: Name = Name.random(), customizeConsumer: ConsumerBuilder<EVENT>.() -> ConsumerBuilder<EVENT> = { defaultConsumerCustomization() }, customizeProducer: ProducerBuilder<EVENT>.() -> ProducerBuilder<EVENT> = { defaultProducerCustomization() }): Messages<EVENT, EVENT> {

    val consumer = newMessageConsumer(serde = serde, topic = topic, groupName = groupName, nodeName = nodeName, customizeConsumer)
    val producer = newMessageProducer(serde = serde, topic = topic, groupName = groupName, nodeName = nodeName, customizeProducer)

    return MessagesImplementation(consumer, producer, messageConverter)
}