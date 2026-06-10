package sollecitom.libs.swissknife.messaging.domain.partitioning

import kotlinx.coroutines.flow.SharedFlow

interface PartitionAssignmentChangesAware {

    val partitionAssignmentChanges: SharedFlow<PartitionAssignmentChange>
}