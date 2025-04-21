package sollecitom.libs.swissknife.nats.client

import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import io.nats.client.Message

interface NatsPublisher : Stoppable {

    suspend fun publish(message: Message)

    companion object
}



