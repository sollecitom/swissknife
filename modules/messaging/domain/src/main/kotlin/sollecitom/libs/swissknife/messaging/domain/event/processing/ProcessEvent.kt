package sollecitom.libs.swissknife.messaging.domain.event.processing

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage

fun interface ProcessEvent<in EVENT : Event> {

    context(_: InvocationContext<*>)
    suspend operator fun invoke(event: ReceivedMessage<EVENT>): EventProcessingResult
}