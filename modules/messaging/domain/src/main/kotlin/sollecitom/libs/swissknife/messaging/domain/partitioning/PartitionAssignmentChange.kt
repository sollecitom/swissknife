package sollecitom.libs.swissknife.messaging.domain.partitioning

sealed interface PartitionAssignmentChange {

    val topicPartition: TopicPartition
    val consumerName: String
}