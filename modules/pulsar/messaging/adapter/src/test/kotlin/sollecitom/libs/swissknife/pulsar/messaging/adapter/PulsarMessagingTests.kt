package sollecitom.libs.swissknife.pulsar.messaging.adapter

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import kotlinx.coroutines.runBlocking
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.apache.pulsar.client.api.SubscriptionType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.core.utils.provider
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.messaging.test.utils.message.MessagingTestSpecification
import sollecitom.libs.swissknife.pulsar.test.utils.admin
import sollecitom.libs.swissknife.pulsar.test.utils.client
import sollecitom.libs.swissknife.pulsar.test.utils.newPulsarContainer
import sollecitom.libs.swissknife.pulsar.utils.configureNamespace
import sollecitom.libs.swissknife.pulsar.utils.createNamespace
import sollecitom.libs.swissknife.pulsar.utils.createTenant
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
class PulsarMessagingTests : MessagingTestSpecification, CoreDataGenerator by CoreDataGenerator.provider() {

    private val pulsar = newPulsarContainer().apply { start() }
    private val client = pulsar.client()
    private val admin = pulsar.admin()

    override fun newTopic(tenant: Name, namespaceName: Name, namespace: Topic.Namespace, name: Name, persistent: Boolean): Topic = Topic.of(persistent, namespace, name).also { admin.ensureTopicExists(it, isAllowAutoUpdateSchema = true) }

    override fun newMessageProducer(topic: Topic, name: String): MessageProducer<String> = client.newMessageProducer(Name(name), topic, Schema.STRING)

    override fun newMessageConsumer(topics: Set<Topic>, subscriptionName: String, name: String): MessageConsumer<String> = client.newMessageConsumer(topics) {
        client.newConsumer(Schema.STRING).topics(it).subscriptionName(subscriptionName).consumerName(name).subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
    }.also { runBlocking { it.start() } }

    @Test
    fun `a plain consumer keeps the configured subscription type`() = test {

        val topic = newTopic()
        val subscriptionName = Name.random().value
        val consumer = client.newMessageConsumer(topic) { client.newConsumer(Schema.STRING).topics(it).subscriptionName(subscriptionName).subscriptionType(SubscriptionType.Shared) }

        consumer.start()
        val subscriptionType = admin.topics().getPartitionedStats(topic.fullName.value, false).subscriptions.getValue(subscriptionName).type
        consumer.stop()

        assertThat(subscriptionType).isEqualTo(SubscriptionType.Shared.name)
    }

    @Test
    fun `a partition-assignment-aware consumer forces a failover subscription`() = test {

        val topic = newTopic()
        val subscriptionName = Name.random().value
        val consumer = client.newPartitionAssignmentAwareMessageConsumer(setOf(topic)) { client.newConsumer(Schema.STRING).topics(it).subscriptionName(subscriptionName).subscriptionType(SubscriptionType.Shared) }

        consumer.start()
        val subscriptionType = admin.topics().getPartitionedStats(topic.fullName.value, false).subscriptions.getValue(subscriptionName).type
        consumer.stop()

        assertThat(subscriptionType).isEqualTo(SubscriptionType.Failover.name)
    }

    @Test
    fun `stopping a consumer or producer that never started does not connect`() = test(timeout = 10.seconds) {

        val unreachable = PulsarClient.builder().serviceUrl("pulsar://localhost:1").build()
        val topic = Topic.persistent(Name.random())
        val consumer = unreachable.newMessageConsumer(topic) { unreachable.newConsumer(Schema.STRING).topics(it).subscriptionName("never") }
        val producer = unreachable.newMessageProducer(Name.random(), topic, Schema.STRING)

        val result = runCatching {
            consumer.stop()
            producer.stop()
        }

        assertThat(result).isSuccess()
        unreachable.close()
    }

    @Test
    fun `auto-created topics are partitioned`() {

        val tenant = Name.random().value
        val namespace = Name.random().value
        admin.createTenant(tenant)
        admin.createNamespace(tenant, namespace)

        admin.configureNamespace(tenant, namespace, allowTopicCreation = true)
        val autoTopicCreation = admin.namespaces().getAutoTopicCreation("$tenant/$namespace")

        assertThat(autoTopicCreation.topicType).isEqualTo("partitioned")
        assertThat(autoTopicCreation.defaultNumPartitions).isEqualTo(1)
    }

    @AfterAll
    fun stopPulsar() {
        client.close()
        admin.close()
        pulsar.stop()
    }
}
