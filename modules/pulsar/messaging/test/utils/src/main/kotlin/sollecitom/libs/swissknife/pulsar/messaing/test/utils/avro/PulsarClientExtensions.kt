package sollecitom.libs.swissknife.pulsar.messaing.test.utils.avro

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.avro.utils.newMessageConsumer
import sollecitom.libs.swissknife.pulsar.avro.utils.newMessageProducer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient

context(_: RandomGenerator)
fun <VALUE : Any> PulsarClient.newMessageConsumer(serde: AvroSerde<VALUE>, topic: Topic, groupName: Name = Name.random(), nodeName: Name = Name.random(), customize: ConsumerBuilder<VALUE>.() -> ConsumerBuilder<VALUE> = { this }): MessageConsumer<VALUE> = newMessageConsumer(serde, setOf(topic), groupName, nodeName, customize)

context(_: RandomGenerator)
fun <VALUE : Any> PulsarClient.newMessageConsumer(serde: AvroSerde<VALUE>, topics: Set<Topic>, groupName: Name = Name.random(), nodeName: Name = Name.random(), customize: ConsumerBuilder<VALUE>.() -> ConsumerBuilder<VALUE> = { this }): MessageConsumer<VALUE> = newMessageConsumer(serde = serde, topics = topics, instanceInfo = InstanceInfo(nodeName = nodeName, groupName = groupName), customize = customize)

context(_: RandomGenerator)
fun <VALUE : Any> PulsarClient.newMessageProducer(serde: AvroSerde<VALUE>, topic: Topic, groupName: Name = Name.random(), nodeName: Name = Name.random(), customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { this }): MessageProducer<VALUE> = newMessageProducer(serde = serde, topic = topic, instanceInfo = InstanceInfo(nodeName = nodeName, groupName = groupName), customize = customize)

