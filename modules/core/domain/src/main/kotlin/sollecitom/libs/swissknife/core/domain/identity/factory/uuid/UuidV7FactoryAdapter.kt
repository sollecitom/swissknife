package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal object UuidV7FactoryAdapter : SortableTimestampedUniqueIdentifierFactory<UUIDv7> {

    override fun invoke(): UUIDv7 = UUIDv7(delegate = Uuid.generateV7())

    override fun invoke(timestamp: Instant): UUIDv7 = UUIDv7(delegate = Uuid.generateV7NonMonotonicAt(timestamp))
}
