package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder

fun <V> ConsumerBuilder<V>.topics(vararg topics: Topic): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ConsumerBuilder<V>.topics(topics: Set<Topic>): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ProducerBuilder<V>.topic(topic: Topic): ProducerBuilder<V> = topic(topic.fullName.value)