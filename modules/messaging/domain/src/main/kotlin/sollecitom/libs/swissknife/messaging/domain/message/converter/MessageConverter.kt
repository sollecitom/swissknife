package sollecitom.libs.swissknife.messaging.domain.message.converter

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer

/** Converts domain values into outbound messages, optionally linking to parent/originating messages. */
interface MessageConverter<VALUE> {

    fun toOutboundMessage(value: VALUE, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null): Message<VALUE>
}

context(converter: MessageConverter<VALUE>)
suspend fun <VALUE> MessageProducer<VALUE>.produce(value: VALUE, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null) = produce(converter.toOutboundMessage(value = value, parentMessageId = parentMessageId, originatingMessageId = originatingMessageId))

context(converter: MessageConverter<VALUE>)
suspend fun <VALUE> MessageProducer<VALUE>.produce(value: VALUE, parent: ReceivedMessage<*>) = produce(converter.toOutboundMessage(value = value, parentMessageId = parent.id, originatingMessageId = parent.context.originatingMessageId ?: parent.context.parentMessageId))

context(converter: MessageConverter<VALUE>)
fun <VALUE> VALUE.asMessage(parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null): Message<VALUE> = converter.toOutboundMessage(this, parentMessageId, originatingMessageId)