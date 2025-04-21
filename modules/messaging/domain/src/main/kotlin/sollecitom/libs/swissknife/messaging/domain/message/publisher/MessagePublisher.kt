package sollecitom.libs.swissknife.messaging.domain.message.publisher

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.originatingMessageIdOrFallback

interface MessagePublisher<in VALUE> : Startable, Stoppable {

    context(_: InvocationContext<*>)
    suspend fun publish(value: VALUE, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null)

    companion object
}

context(_: InvocationContext<*>)
suspend fun <VALUE> MessagePublisher<VALUE>.publish(event: VALUE, parent: ReceivedMessage<*>) = publish(value = event, parentMessageId = parent.id, originatingMessageId = parent.originatingMessageIdOrFallback)

context(_: InvocationContext<*>, parentMessage: ReceivedMessage<*>)
suspend fun <VALUE> MessagePublisher<VALUE>.publishWithParentMessageContext(event: VALUE) = publish(event = event, parent = parentMessage)