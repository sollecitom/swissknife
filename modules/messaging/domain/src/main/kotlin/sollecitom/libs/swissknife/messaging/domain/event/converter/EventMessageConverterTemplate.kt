package sollecitom.libs.swissknife.messaging.domain.event.converter

import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.OutboundMessage
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames

abstract class EventMessageConverterTemplate<EVENT : Event>(private val propertyNames: MessagePropertyNames) : MessageConverter<EVENT> {

    final override fun toOutboundMessage(value: EVENT, parentMessageId: Message.Id?, originatingMessageId: Message.Id?): Message<EVENT> {

        val key = key(value)
        val properties = standardEventProperties(value) + additionalProperties(value)
        val context = Message.Context(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId)
        return OutboundMessage(key = key, value = value, properties = properties, context = context)
    }

    protected abstract fun key(event: EVENT): String

    protected fun additionalProperties(event: EVENT): Map<String, String> = emptyMap()

    protected fun standardEventProperties(event: EVENT): Map<String, String> = mapOf(
        propertyNames.forEvents.type to event.type.stringValue
        // TODO evaluate adding more things here e.g. customer ID
    )
}