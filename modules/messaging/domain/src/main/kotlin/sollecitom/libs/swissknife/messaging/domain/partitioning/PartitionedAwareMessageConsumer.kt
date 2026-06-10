package sollecitom.libs.swissknife.messaging.domain.partitioning

import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer

interface PartitionedAwareMessageConsumer<out VALUE> : MessageConsumer<VALUE>, PartitionAssignmentChangesAware