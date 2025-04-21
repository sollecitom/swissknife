package sollecitom.libs.swissknife.messaging.domain.partitioning

import kotlinx.coroutines.flow.Flow

interface PartitionAssignmentChangesAware {

    val partitionAssignmentChanges: Flow<PartitionAssignmentChange>
}