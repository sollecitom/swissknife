package sollecitom.libs.swissknife.pulsar.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import org.apache.pulsar.client.api.*
import java.net.URI

suspend fun TypedMessageBuilder<*>.produce(): MessageIdAdv = sendAsync().await() as MessageIdAdv

suspend fun <VALUE> Consumer<VALUE>.consume(): Message<VALUE> = receiveAsync().await()

val <VALUE> Consumer<VALUE>.messages: Flow<Message<VALUE>>
    get() = flow {
        while (currentCoroutineContext().isActive) {
            val message = consume()
            emit(message)
        }
    }

val Message<*>.id: MessageIdAdv get() = messageId as MessageIdAdv

fun ClientBuilder.brokerURI(brokerURI: URI): ClientBuilder = serviceUrl(brokerURI.toString())