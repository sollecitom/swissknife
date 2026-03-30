package sollecitom.libs.swissknife.pulsar.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import org.apache.pulsar.client.api.*
import java.net.URI

/** Sends a Pulsar message asynchronously, suspending until completion. */
suspend fun TypedMessageBuilder<*>.produce(): MessageIdAdv = sendAsync().await() as MessageIdAdv

/** Receives a single message from this Pulsar consumer, suspending until one is available. */
suspend fun <VALUE> Consumer<VALUE>.consume(): Message<VALUE> = receiveAsync().await()

/** Emits messages from this Pulsar consumer as a [Flow], continuously receiving while the coroutine is active. */
val <VALUE> Consumer<VALUE>.messages: Flow<Message<VALUE>>
    get() = flow {
        while (currentCoroutineContext().isActive) {
            val message = consume()
            emit(message)
        }
    }

/** The message ID cast to [MessageIdAdv] for access to advanced properties (e.g., partition, ledger). */
val Message<*>.id: MessageIdAdv get() = messageId as MessageIdAdv

/** Sets the Pulsar broker service URL from a [URI]. */
fun ClientBuilder.brokerURI(brokerURI: URI): ClientBuilder = serviceUrl(brokerURI.toString())