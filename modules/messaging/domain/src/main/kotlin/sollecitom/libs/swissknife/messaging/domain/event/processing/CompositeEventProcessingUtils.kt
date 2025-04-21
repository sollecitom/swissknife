package sollecitom.libs.swissknife.messaging.domain.event.processing

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage

context(_: InvocationContext<*>)
inline fun <EVENT : Event.Composite<DATA>, DATA : Event.Data> ReceivedMessage<EVENT>.processAsCompositeEvent(action: context(ReceivedMessage<*>) (data: DATA, event: EVENT) -> Unit): EventProcessingResult = runCatching { action(this, value.data, value) }.map { EventProcessingResult.Success }.getOrElse(Throwable::asProcessingFailure)