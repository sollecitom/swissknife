package sollecitom.libs.swissknife.messaging.test.utils.message

import assertk.Assert
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.message.converter.asMessage
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.messaging.test.utils.topic.create
import kotlinx.coroutines.*
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class ReceivedMessageSpy<VALUE>(override val id: Message.Id, override val key: String, override val value: VALUE, override val properties: Map<String, String>, override val context: Message.Context, override val topic: Topic, override val producerName: Name, override val publishedAt: Instant, private val acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit, private val acknowledgeAsFailed: suspend (ReceivedMessage<VALUE>) -> Unit) : ReceivedMessage<VALUE> {

    private var acknowledgedSuccessfully = false
    private var acknowledgedAsFailed = false
    val wasAcknowledgedSuccessfully: Boolean get() = acknowledgedSuccessfully
    val wasAcknowledgedAsFailed: Boolean get() = acknowledgedAsFailed
    override val rawData: ByteArray get() = error("Received message spy data isn't backed by a byte array")


    suspend fun awaitSuccessfulAck(pollingPeriod: Duration = 20.milliseconds) = coroutineScope {

        while (isActive) {
            if (wasAcknowledgedSuccessfully) return@coroutineScope
            delay(pollingPeriod)
        }
    }

    suspend fun awaitNegativeAck(pollingPeriod: Duration = 20.milliseconds) = coroutineScope {

        while (isActive) {
            if (wasAcknowledgedAsFailed) return@coroutineScope
            delay(pollingPeriod)
        }
    }

    suspend fun awaitAnyAck(pollingPeriod: Duration = 20.milliseconds) = coroutineScope {

        while (isActive) {
            if (wasAcknowledgedSuccessfully || wasAcknowledgedAsFailed) return@coroutineScope
            delay(pollingPeriod)
        }
    }

    override suspend fun acknowledge() {
        acknowledge(this)
        acknowledgedSuccessfully = true
    }

    override suspend fun acknowledgeAsFailed() {
        acknowledgeAsFailed(this)
        acknowledgedAsFailed = true
    }

    override fun toString() = "ReceivedMessageSpy(id=$id, properties=$properties, topic=$topic, context=$context, wasAcknowledgedSuccessfully=$wasAcknowledgedSuccessfully, wasAcknowledgedAsFailed=$wasAcknowledgedAsFailed)"
}

context(_: UniqueIdGenerator, time: TimeGenerator, _: RandomGenerator)
fun <VALUE> ReceivedMessage.Companion.inMemorySpy(value: VALUE, key: String = Name.random().value, topic: Topic = Topic.create(), partition: Topic.Partition? = null, properties: Map<String, String> = emptyMap(), context: Message.Context = Message.Context(), producerName: Name = Name.random(), id: Message.Id = Message.Id.ulid(topic = topic, partition = partition), publishedAt: Instant = time.now(), acknowledgeAsFailed: suspend (ReceivedMessage<VALUE>) -> Unit = {}, acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit = {}) = ReceivedMessageSpy(id = id, key = key, value, properties = properties, context = context, topic = topic, producerName = producerName, publishedAt = publishedAt, acknowledge = acknowledge, acknowledgeAsFailed = acknowledgeAsFailed)

context(_: UniqueIdGenerator, time: TimeGenerator, _: RandomGenerator)
fun <VALUE> Message<VALUE>.asReceivedMessageSpy(topic: Topic = Topic.create(), partition: Topic.Partition? = null, producerName: Name = Name.random(), id: Message.Id = Message.Id.ulid(topic = topic, partition = partition), publishedAt: Instant = time.now(), acknowledgeAsFailed: suspend (ReceivedMessage<VALUE>) -> Unit = {}, acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit = {}) = ReceivedMessage.inMemorySpy(value = this.value, key = this.key, properties = this.properties, context = this.context, topic = topic, producerName = producerName, id = id, partition = partition, publishedAt = publishedAt, acknowledgeAsFailed = acknowledgeAsFailed, acknowledge = acknowledge)

context(_: UniqueIdGenerator, time: TimeGenerator, _: RandomGenerator, _: MessageConverter<VALUE>)
fun <VALUE> VALUE.asReceivedMessageSpy(topic: Topic = Topic.create(), partition: Topic.Partition? = null, producerName: Name = Name.random(), id: Message.Id = Message.Id.ulid(topic = topic, partition = partition), publishedAt: Instant = time.now(), parentMessageId: Message.Id? = null, originatingMessageId: Message.Id? = null, acknowledgeAsFailed: suspend (ReceivedMessage<VALUE>) -> Unit = {}, acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit = {}) = asMessage(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId).asReceivedMessageSpy(topic = topic, producerName = producerName, id = id, partition = partition, publishedAt = publishedAt, acknowledgeAsFailed = acknowledgeAsFailed, acknowledge = acknowledge)

fun Assert<ReceivedMessageSpy<*>>.wasAcknowledgedSuccessfully() = given { message ->

    assertThat(message.wasAcknowledgedSuccessfully).isTrue()
}

fun Assert<ReceivedMessageSpy<*>>.wasNotAcknowledgedSuccessfully() = given { message ->

    assertThat(message.wasAcknowledgedSuccessfully).isFalse()
}

fun Assert<ReceivedMessageSpy<*>>.wasAcknowledgedAsFailed() = given { message ->

    assertThat(message.wasAcknowledgedAsFailed).isTrue()
}

fun Assert<ReceivedMessageSpy<*>>.wasNotAcknowledgedAsFailed() = given { message ->

    assertThat(message.wasAcknowledgedAsFailed).isFalse()
}

context(scope: CoroutineScope)
suspend fun Iterable<ReceivedMessageSpy<*>>.waitUntilAllAcked(pollingPeriod: Duration = 20.milliseconds) = map { scope.launch { it.awaitAnyAck(pollingPeriod) } }.joinAll()

context(scope: CoroutineScope)
suspend fun Iterable<ReceivedMessageSpy<*>>.waitUntilAllAckedSuccessfully(pollingPeriod: Duration = 20.milliseconds) = map { scope.launch { it.awaitSuccessfulAck(pollingPeriod) } }.joinAll()

context(scope: CoroutineScope)
suspend fun Iterable<ReceivedMessageSpy<*>>.waitUntilAllAckedAsFailed(pollingPeriod: Duration = 20.milliseconds) = map { scope.launch { it.awaitNegativeAck(pollingPeriod) } }.joinAll()
