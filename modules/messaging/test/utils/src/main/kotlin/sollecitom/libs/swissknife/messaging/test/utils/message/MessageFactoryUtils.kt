package sollecitom.libs.swissknife.messaging.test.utils.message

import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.OutboundMessage

fun <VALUE> outboundMessage(key: String, value: VALUE, properties: Map<String, String> = emptyMap(), context: Message.Context = Message.Context.ORIGINATING): OutboundMessage<VALUE> = OutboundMessage(key = key, value = value, properties = properties, context = context)