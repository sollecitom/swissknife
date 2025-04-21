package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol.MessageContextPropertiesSerde
import sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol.adapted
import sollecitom.libs.swissknife.pulsar.utils.produce
import org.apache.pulsar.client.api.Producer

suspend fun <VALUE> Producer<VALUE>.produce(message: Message<VALUE>): Message.Id {

    val contextProperties = MessageContextPropertiesSerde.serialize(message.context)
    return newMessage().key(message.key).value(message.value).properties(message.properties + contextProperties).produce().adapted(topic = Topic.parse(topic))
}