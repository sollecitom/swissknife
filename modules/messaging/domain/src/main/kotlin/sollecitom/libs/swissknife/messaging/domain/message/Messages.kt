package sollecitom.libs.swissknife.messaging.domain.message

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.messaging.domain.event.utils.hasType
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.consumer.messages
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.message.converter.produce
import sollecitom.libs.swissknife.messaging.domain.message.correlation.utils.wasCausedBy
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface Messages<out OUT : Event, in IN : Event> : Startable, Stoppable {

    val values: Flow<ReceivedMessage<OUT>>

    suspend fun produce(value: IN, parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null): Message.Id
}

class MessagesImplementation<out OUT : Event, IN : Event>(private val consumer: MessageConsumer<OUT>, private val producer: MessageProducer<IN>, messageConverter: MessageConverter<IN>) : Messages<OUT, IN>, MessageConverter<IN> by messageConverter {

    override val values: Flow<ReceivedMessage<OUT>> get() = consumer.messages

    override suspend fun produce(value: IN, parentMessageId: Message.Id?, originatingMessageId: Message.Id?) = producer.produce(value, parentMessageId, originatingMessageId)

    override suspend fun start() {
        producer.start()
        consumer.start()
    }

    override suspend fun stop() {
        consumer.stop()
        producer.stop()
    }

    companion object
}

context(_: MessagePropertyNames)
suspend fun <OUT : Event, IN : Event> Messages<OUT, IN>.produceAndAwaitSuccessor(event: IN, successorType: Happening.Type): Pair<Message.Id, ReceivedMessage<OUT>> {

    val messageId = produce(event)
    val successor = values.first { it.hasType(successorType) && it.wasCausedBy(event) }.apply { acknowledge() }
    return messageId to successor
}