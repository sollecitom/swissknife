package sollecitom.libs.swissknife.messaging.domain.partitioning

import sollecitom.libs.swissknife.core.domain.position.Index

data class TopicPartition(val topic: String, val partition: Index)