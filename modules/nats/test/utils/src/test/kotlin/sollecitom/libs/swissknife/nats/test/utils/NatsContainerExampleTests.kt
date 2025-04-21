package sollecitom.libs.swissknife.nats.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.nats.client.toMultiMap
import sollecitom.libs.swissknife.test.utils.assertions.containsSameMultipleEntriesAs
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
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
}