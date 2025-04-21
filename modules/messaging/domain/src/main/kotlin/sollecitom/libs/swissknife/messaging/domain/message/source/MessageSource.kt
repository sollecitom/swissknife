package sollecitom.libs.swissknife.messaging.domain.message.source

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import kotlinx.coroutines.flow.Flow

interface MessageSource<out VALUE> : Startable, Stoppable {

    val messages: Flow<ReceivedMessage<VALUE>>

    companion object
}