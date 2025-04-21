package sollecitom.libs.swissknife.messaging.domain.partitioning

import sollecitom.libs.swissknife.core.domain.position.Index

data class PartitionUnassigned(override val topicPartition: TopicPartition, override val consumerName: String) : PartitionAssignmentChange {

    constructor(partitionIndex: Index, topic: String, consumerName: String) : this(TopicPartition(topic, partitionIndex), consumerName)
}