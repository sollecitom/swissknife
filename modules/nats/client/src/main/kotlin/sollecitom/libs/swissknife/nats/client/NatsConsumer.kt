package sollecitom.libs.swissknife.nats.client

import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import io.nats.client.Message
import kotlinx.coroutines.flow.Flow

/** Consumes messages from NATS subjects as a [Flow]. Subscribes on first collection and manages its own connection lifecycle. */
interface NatsConsumer : Stoppable, AutoCloseable {

    val messages: Flow<Message>

    override fun close() = stopBlocking()

    companion object
}