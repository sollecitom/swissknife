package sollecitom.libs.swissknife.messaging.domain.message

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.logging.utils.withLoggingContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.forkAndLogInvocationContext
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

interface ReceivedMessage<out VALUE> : Message<VALUE>, Comparable<ReceivedMessage<*>> {

    val id: Message.Id
    val publishedAt: Instant
    val producerName: Name
    val topic: Topic get() = id.topic

    suspend fun acknowledge()

    suspend fun acknowledgeAsFailed()

    override fun compareTo(other: ReceivedMessage<*>) = id.compareTo(other.id)

    companion object
}

@Suppress("UNCHECKED_CAST")
fun <VALUE, V : VALUE> ReceivedMessage<VALUE>.into(): ReceivedMessage<V> = this as ReceivedMessage<V>

fun ReceivedMessage<*>.forkContext(): Message.Context = context.fork(parentMessageId = id)

context(scope: CoroutineScope, _: UniqueIdGenerator, _: TimeGenerator, _: Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.processWithForkedContext(start: CoroutineStart = LAZY, convertContext: (InvocationContext<*>) -> String, action: suspend context(InvocationContext<*>) (message: ReceivedMessage<EVENT>) -> Unit): Job = processWithForkedContext(scope, start, convertContext, action)

context(_: UniqueIdGenerator, _: TimeGenerator, _: Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.processWithForkedContext(scope: CoroutineScope, start: CoroutineStart = LAZY, convertContext: (InvocationContext<*>) -> String, action: suspend context(InvocationContext<*>) (message: ReceivedMessage<EVENT>) -> Unit): Job = scope.launch(start = start) {
    onEach { message ->
        val invocationContext = message.value.forkAndLogInvocationContext()
        withLoggingContext(invocationContext, convertContext) {
            action(invocationContext, message)
        }
    }.collect()
}

val ReceivedMessage<*>.originatingMessageIdOrFallback get() = context.originatingMessageId ?: context.parentMessageId ?: id