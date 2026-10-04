package sollecitom.libs.swissknife.nats.test.utils

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.nats.client.toMultiMap
import sollecitom.libs.swissknife.test.utils.assertions.containsSameMultipleEntriesAs
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
class NatsContainerExampleTests {

    private val timeout = 10.seconds
    private val nats = NatsContainer()

    @BeforeAll
    fun beforeAll() = nats.start()

    @AfterAll
    fun afterAll() = nats.stop()

    @Test
    fun `consuming and publishing messages`() = test(timeout = timeout) {

        val subject = "a-subject"
        val publisher = nats.newPublisher()
        val consumer = nats.newConsumer(subject)
        val payload = "hello world"
        val headers = buildMap {
            put("header-key", buildList {
                add("headerValue1")
                add("headerValue2")
            })
        }

        val receiving = async(start = UNDISPATCHED) { consumer.messages.first() }
        val outboundMessage = NatsMessage(subject, null, Headers().put(headers), payload.toByteArray())
        publisher.publish(outboundMessage)
        val receivedMessage = receiving.await()
        publisher.stop()
        consumer.stop()

        assertThat(receivedMessage.subject).isEqualTo(subject)
        assertThat(String(receivedMessage.data)).isEqualTo(payload)
        assertThat(receivedMessage.headers.toMultiMap()).containsSameMultipleEntriesAs(headers)
    }

    @Test
    fun `collecting messages more than once delivers each message once`() = test(timeout = timeout) {

        val subject = "another-subject"
        val publisher = nats.newPublisher()
        val consumer = nats.newConsumer(subject)

        repeat(2) { round ->
            val earlierCollection = async(start = UNDISPATCHED) { consumer.messages.first() }
            publisher.publish(NatsMessage(subject, null, "earlier-$round".toByteArray()))
            earlierCollection.await()
        }
        val laterCollection = async(start = UNDISPATCHED) { consumer.messages.take(2).map { String(it.data) }.toList() }
        publisher.publish(NatsMessage(subject, null, "first".toByteArray()))
        publisher.publish(NatsMessage(subject, null, "second".toByteArray()))
        val received = laterCollection.await()
        publisher.stop()
        consumer.stop()

        assertThat(received).containsExactly("first", "second")
    }
}