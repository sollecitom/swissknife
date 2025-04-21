package sollecitom.libs.swissknife.nats.client

import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking
import io.nats.client.Message
import kotlinx.coroutines.flow.Flow

interface NatsConsumer : Stoppable, AutoCloseable {

    val messages: Flow<Message>

    override fun close() = stopBlocking()

    companion object
}