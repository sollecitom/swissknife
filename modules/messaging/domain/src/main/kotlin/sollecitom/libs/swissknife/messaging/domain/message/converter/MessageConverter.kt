package sollecitom.libs.swissknife.messaging.domain.message.converter

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer

interface MessageConverter<VALUE> {

    fun toOutboundMessage(value: VALUE, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null): Message<VALUE>
}

fun <VALUE> MessageConverter<VALUE>.toOutboundMessage(value: VALUE, parentMessage: Message<VALUE>) = toOutboundMessage(value = value, parentMessageId = parentMessage.context.parentMessageId, originatingMessageId = parentMessage.context.originatingMessageId)

context(MessageConverter<VALUE>)
suspend fun <VALUE> MessageProducer<VALUE>.produce(value: VALUE, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null) = produce(toOutboundMessage(value = value, parentMessageId = parentMessageId, originatingMessageId = originatingMessageId))

context(MessageConverter<VALUE>)
suspend fun <VALUE> MessageProducer<VALUE>.produce(value: VALUE, parent: ReceivedMessage<*>) = produce(toOutboundMessage(value = value, parentMessageId = parent.id, originatingMessageId = parent.context.originatingMessageId ?: parent.context.parentMessageId))

context(MessageConverter<VALUE>)
fun <VALUE> VALUE.asMessage(parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null): Message<VALUE> = toOutboundMessage(this, parentMessageId, originatingMessageId)