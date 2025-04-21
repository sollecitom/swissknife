package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.core.domain.position.Index
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.consumer.PartitionAssignmentChangesAwareMessageConsumer
import sollecitom.libs.swissknife.messaging.domain.partitioning.PartitionAssigned
import sollecitom.libs.swissknife.messaging.domain.partitioning.PartitionAssignmentChange
import sollecitom.libs.swissknife.messaging.domain.partitioning.PartitionUnassigned
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.apache.pulsar.client.api.*

private class PulsarPartitionChangeAwareMessageConsumer<out VALUE>(override val topics: Set<Topic>, initializeConsumer: (Set<Topic>) -> ConsumerBuilder<VALUE>) : PartitionAssignmentChangesAwareMessageConsumer<VALUE> {

    private val listener by lazy { EmittingConsumerEventListener() }
    private val messageConsumer by lazy { PulsarMessageConsumer(topics) { initializeConsumer(topics).subscriptionType(SubscriptionType.Failover).consumerEventListener(listener) } }
    override val partitionAssignmentChanges: Flow<PartitionAssignmentChange> get() = listener.changes

    override val name get() = messageConsumer.name
    override val subscriptionName get() = messageConsumer.subscriptionName

    override suspend fun receive() = messageConsumer.receive()

    override suspend fun start() = messageConsumer.start()

    override suspend fun stop() = messageConsumer.stop()

    override fun close() = messageConsumer.close()

    private class EmittingConsumerEventListener : ConsumerEventListener {

        val changes = MutableSharedFlow<PartitionAssignmentChange>()

        override fun becameActive(consumer: Consumer<*>, partitionId: Int) = emit(change = PartitionAssigned(partitionId.let(::Index), consumer.topic, consumer.consumerName))

        override fun becameInactive(consumer: Consumer<*>, partitionId: Int) = emit(change = PartitionUnassigned(partitionId.let(::Index), consumer.topic, consumer.consumerName))

        private fun emit(change: PartitionAssignmentChange) = runBlocking {

            logger.debug { "Partition with index ${change.topicPartition.partition.value} on topic '${change.topicPartition.topic}' was ${if (change is PartitionAssigned) "assigned to" else "unassigned from"} consumer '${change.consumerName}'" }
            changes.emit(change)
        }

        companion object : Loggable()
    }

    companion object
}

fun <VALUE> PulsarClient.newMessageConsumer(topics: Set<Topic>, initializeConsumer: (Set<Topic>) -> ConsumerBuilder<VALUE>): PartitionAssignmentChangesAwareMessageConsumer<VALUE> = PulsarPartitionChangeAwareMessageConsumer(topics, initializeConsumer)

fun <VALUE> PulsarClient.newMessageConsumer(topic: Topic, initializeConsumer: (Set<Topic>) -> ConsumerBuilder<VALUE>) = newMessageConsumer(topics = setOf(topic), initializeConsumer)