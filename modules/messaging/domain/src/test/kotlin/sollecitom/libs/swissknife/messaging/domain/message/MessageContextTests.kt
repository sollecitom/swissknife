package sollecitom.libs.swissknife.messaging.domain.message

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

@TestInstance(PER_CLASS)
class MessageContextTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class OriginatingContext {

        @Test
        fun `an originating context has no parent or originating message ID`() {

            val context = Message.Context()

            assertThat(context.isOriginating).isTrue()
            assertThat(context.parentMessageId).isNull()
            assertThat(context.originatingMessageId).isNull()
        }

        @Test
        fun `the ORIGINATING constant is originating`() {

            assertThat(Message.Context.ORIGINATING.isOriginating).isTrue()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NonOriginatingContext {

        @Test
        fun `a non-originating context has both parent and originating message IDs`() {

            val context = Message.Context(parentMessageId = messageId, originatingMessageId = messageId)

            assertThat(context.isOriginating).isFalse()
        }

        @Test
        fun `specifying only parent without originating is not allowed`() {

            val result = runCatching { Message.Context(parentMessageId = messageId, originatingMessageId = null) }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }

        @Test
        fun `specifying only originating without parent is not allowed`() {

            val result = runCatching { Message.Context(parentMessageId = null, originatingMessageId = messageId) }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Forking {

        @Test
        fun `forking an originating context sets the parent as originating`() {

            val context = Message.Context()

            val forked = context.fork(parentMessageId = messageId)

            assertThat(forked.parentMessageId).isEqualTo(messageId)
            assertThat(forked.originatingMessageId).isEqualTo(messageId)
        }

        @Test
        fun `forking a non-originating context preserves the originating message ID`() {

            val originalOriginatingId = anotherMessageId
            val context = Message.Context(parentMessageId = messageId, originatingMessageId = originalOriginatingId)

            val forked = context.fork(parentMessageId = yetAnotherMessageId)

            assertThat(forked.parentMessageId).isEqualTo(yetAnotherMessageId)
            assertThat(forked.originatingMessageId).isEqualTo(originalOriginatingId)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class OutboundMessageCreation {

        @Test
        fun `creating an outbound message`() {

            val message = OutboundMessage(key = "key-1", value = "payload", properties = mapOf("p1" to "v1"), context = Message.Context())

            assertThat(message.key).isEqualTo("key-1")
            assertThat(message.value).isEqualTo("payload")
            assertThat(message.properties["p1"]).isEqualTo("v1")
            assertThat(message.context.isOriginating).isTrue()
        }

        @Test
        fun `outbound message rawData throws error`() {

            val message = OutboundMessage(key = "key", value = "val", properties = emptyMap(), context = Message.Context())

            val result = runCatching { message.rawData }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }
    }

    private val topic = Topic.persistent(Name("test-topic"))
    private val messageId = TestMessageId("msg-1", topic)
    private val anotherMessageId = TestMessageId("msg-2", topic)
    private val yetAnotherMessageId = TestMessageId("msg-3", topic)

    private data class TestMessageId(override val stringRepresentation: String, override val topic: Topic, override val partition: Topic.Partition? = null) : Message.Id {
        override fun compareTo(other: Message.Id) = stringRepresentation.compareTo(other.stringRepresentation)
    }
}
