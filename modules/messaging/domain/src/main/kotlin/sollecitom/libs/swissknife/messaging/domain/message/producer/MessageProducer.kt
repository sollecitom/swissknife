package sollecitom.libs.swissknife.messaging.domain.message.producer

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

interface MessageProducer<in VALUE> : Startable, Stoppable, AutoCloseable {

    val name: Name
    val topic: Topic

    suspend fun produce(message: Message<VALUE>): Message.Id
}