package sollecitom.libs.swissknife.messaging.test.utils.context

import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.Message

data class EventAndParentContext<out EVENT : Event>(val event: EVENT, val parentMessageContext: Message.Context)

fun <EVENT : Event> EVENT.withParentMessageContext(context: Message.Context) = EventAndParentContext(event = this, parentMessageContext = context)

fun <EVENT : Event> EVENT.withParentMessage(message: Message<*>) = withParentMessageContext(context = message.context)