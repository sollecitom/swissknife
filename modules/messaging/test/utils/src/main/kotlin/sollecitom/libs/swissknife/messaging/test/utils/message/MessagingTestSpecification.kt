package sollecitom.libs.swissknife.messaging.test.utils.message

import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.OutboundMessage
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.consumer.messages
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface MessagingTestSpecification : CoreDataGenerator {

    val timeout: Duration get() = 30.seconds

    @Test
    fun `sending and receiving multiple messages using the messaging API wrapper types`() = test(timeout = timeout) {

        val topic = newTopic()
        val consumer = newMessageConsumer(topic)
        val producer = newMessageProducer(topic)

        val producedMessages = mutableListOf<Message<String>>()
        val originatingMessage = OutboundMessage("key-0", "value-0", emptyMap(), Message.Context())
        val originatingMessageId = producer.produce(originatingMessage).also { producedMessages += originatingMessage }
        var parentMessageId = originatingMessageId
        val messagesCount = 5
        (1..<messagesCount).forEach { index ->
            val message = OutboundMessage("key-$index", "value-$index", emptyMap(), Message.Context(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId))
            parentMessageId = producer.produce(message).also { producedMessages += message }
        }

        val receivedMessages = consumer.messages.take(messagesCount).toList()

        assertThat(receivedMessages).hasSameSizeAs(producedMessages)
        receivedMessages.forEachIndexed { index, receivedMessage ->
            assertThat(receivedMessage).matches(producedMessages[index])
            assertThat(receivedMessage.producerName).isEqualTo(producer.name)
        }
    }

    fun newTopic(tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random(), persistent: Boolean = true): Topic

    fun newMessageProducer(topic: Topic, name: String = newId().stringValue): MessageProducer<String>

    fun newMessageConsumer(topics: Set<Topic>, subscriptionName: String = newId().stringValue, name: String = newId().stringValue): MessageConsumer<String>
    fun newMessageConsumer(topic: Topic, subscriptionName: String = newId().stringValue, name: String = newId().stringValue) = newMessageConsumer(topics = setOf(topic), subscriptionName = subscriptionName, name = name)
}