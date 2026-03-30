package sollecitom.libs.swissknife.messaging.domain.message.connector

import sollecitom.libs.swissknife.messaging.domain.message.publisher.MessagePublisher
import sollecitom.libs.swissknife.messaging.domain.message.source.MessageSource

/** Combines [MessageSource] and [MessagePublisher] into a single bidirectional messaging interface. */
interface MessageConnector<VALUE> : MessageSource<VALUE>, MessagePublisher<VALUE> {

    companion object
}