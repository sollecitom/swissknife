package sollecitom.libs.swissknife.pulsar.avro.utils

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.avro.serialization.asPulsarSchema
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultConsumerCustomization
import sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration.defaultProducerCustomization
import sollecitom.libs.swissknife.pulsar.messaging.adapter.newMessageConsumer
import sollecitom.libs.swissknife.pulsar.messaging.adapter.newMessageProducer
import sollecitom.libs.swissknife.pulsar.messaging.adapter.topics
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient

fun <VALUE : Any> PulsarClient.newMessageConsumer(serde: AvroSerde<VALUE>, topics: Set<Topic>, instanceInfo: InstanceInfo, customize: ConsumerBuilder<VALUE>.() -> ConsumerBuilder<VALUE> = { defaultConsumerCustomization() }): MessageConsumer<VALUE> = newMessageConsumer(topics) { newConsumer(serde.asPulsarSchema()).topics(it).consumerName("${instanceInfo.groupName.value}.consumer.${instanceInfo.nodeName.value}").subscriptionName(instanceInfo.groupName.value).customize() }

fun <VALUE : Any> PulsarClient.newMessageConsumer(serde: AvroSerde<VALUE>, topic: Topic, instanceInfo: InstanceInfo, customize: ConsumerBuilder<VALUE>.() -> ConsumerBuilder<VALUE> = { defaultConsumerCustomization() }): MessageConsumer<VALUE> = newMessageConsumer(serde, setOf(topic), instanceInfo, customize)

fun <VALUE : Any> PulsarClient.newMessageProducer(serde: AvroSerde<VALUE>, topic: Topic, name: Name, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { defaultProducerCustomization() }): MessageProducer<VALUE> = newMessageProducer(name, topic, serde.asPulsarSchema(), customize)

fun <VALUE : Any> PulsarClient.newMessageProducer(serde: AvroSerde<VALUE>, topic: Topic, instanceInfo: InstanceInfo, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { defaultProducerCustomization() }): MessageProducer<VALUE> = newMessageProducer(serde, topic, "${instanceInfo.groupName.value}.producer.${instanceInfo.nodeName.value}".let(::Name), customize)