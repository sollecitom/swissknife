package sollecitom.libs.swissknife.messaging.domain.partitioning

import kotlinx.coroutines.flow.SharedFlow
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.partitioning.PartitionAssignmentChange
import sollecitom.libs.swissknife.messaging.domain.partitioning.PartitionAssignmentChangesAware

interface PartitionedAwareMessageConsumer<out VALUE> : MessageConsumer<VALUE>, PartitionAssignmentChangesAware {

    override val partitionAssignmentChanges: SharedFlow<PartitionAssignmentChange>
}