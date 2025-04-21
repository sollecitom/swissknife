package sollecitom.libs.swissknife.messaging.domain.message.correlation.utils

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.ddd.domain.*
import sollecitom.libs.swissknife.messaging.domain.message.Message

val <EVENT : Event> Message<EVENT>.externalInvocationId get() = value.externalInvocationId
val <EVENT : Event> Message<EVENT>.externalActionId get() = value.externalActionId
val <EVENT : Event> Message<EVENT>.externalTrace get() = ExternalInvocationTrace(invocationId = externalInvocationId, actionId = externalActionId)
fun <EVENT : Event> Message<EVENT>.wasTriggeredByInvocationId(externalInvocationId: Id) = value.wasTriggeredByInvocationId(externalInvocationId)

fun <EVENT : Event> Message<EVENT>.wasCausedBy(event: Event) = value.wasCausedBy(event)
fun <EVENT : Event> Message<EVENT>.wasCausedBy(message: Message<Event>) = value.wasTriggeredByInvocationId(message.externalInvocationId)

context(context: InvocationContext<*>)
fun <EVENT : Event> Message<EVENT>.wasCausedByTheInvocationContextInScope() = externalTrace == context.trace.external