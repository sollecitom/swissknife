package sollecitom.libs.swissknife.messaging.domain.message.consumer

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

interface MessageConsumer<out VALUE> : Startable, Stoppable, AutoCloseable {

    val name: Name
    val subscriptionName: Name
    val topics: Set<Topic>

    suspend fun receive(): ReceivedMessage<VALUE>
}

val <VALUE> MessageConsumer<VALUE>.messages: Flow<ReceivedMessage<VALUE>>
    get() = flow {
        while (currentCoroutineContext().isActive) {
            val message = receive()
            emit(message)
        }
    }

