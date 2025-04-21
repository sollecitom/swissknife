package sollecitom.libs.swissknife.pulsar.messaging.adapter.protocol

import sollecitom.libs.swissknife.messaging.domain.message.Message

internal object MessageContextPropertiesSerde {

    private val parentMessageIdProperty = ProtocolProperties.contextualize("MESSAGE-CONTEXT-PARENT-MESSAGE-ID")
    private val originatingMessageIdProperty = ProtocolProperties.contextualize("MESSAGE-CONTEXT-ORIGINATING-MESSAGE-ID")

    fun serialize(context: Message.Context): Map<String, String> = buildMap {

        context.parentMessageId?.let {
            put(parentMessageIdProperty, MessageIdStringSerde.serialize(it))
        }
        context.originatingMessageId?.let {
            put(originatingMessageIdProperty, MessageIdStringSerde.serialize(it))
        }
    }

    fun deserialize(properties: Map<String, String>): Message.Context {

        val parentMessageId = properties[parentMessageIdProperty]?.let(MessageIdStringSerde::deserialize)
        val originatingMessageId = properties[originatingMessageIdProperty]?.let(MessageIdStringSerde::deserialize)
        return Message.Context(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId)
    }
}