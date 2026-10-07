package sollecitom.libs.swissknife.nats.client

import io.nats.client.Connection
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import java.util.concurrent.Executors

private class NatsConsumerAdapter(options: Options, private val subjects: Set<String>, private val subscriptionConfirmationTimeout: Duration) : NatsConsumer {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()
    private val connection by lazy { Nats.connect(Options.Builder(options).executor(executor).build()) }
    override val messages: Flow<Message> = flow {
        val received = Channel<Message>(Channel.BUFFERED)
        val dispatcher = connection.createDispatcher { message -> received.trySendBlocking(message) }
        subjects.forEach(dispatcher::subscribe)
        connection.flush(subscriptionConfirmationTimeout.toJavaDuration())
        try {
            emitAll(received)
        } finally {
            if (connection.status != Connection.Status.CLOSED) connection.closeDispatcher(dispatcher)
        }
    }

    override suspend fun stop() {
        connection.close()
        executor.close()
    }
}

/** Creates a [NatsConsumer] that subscribes to the given [subjects] using the given [options]. */
fun NatsConsumer.Companion.create(options: Options, subjects: Set<String>, subscriptionConfirmationTimeout: Duration = 10.seconds): NatsConsumer = NatsConsumerAdapter(options, subjects, subscriptionConfirmationTimeout)