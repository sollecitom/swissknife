package sollecitom.libs.swissknife.core.domain.identity.factory.ulid

import sollecitom.libs.swissknife.core.domain.identity.ULID
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface UlidVariantSelector {

    val monotonic: SortableTimestampedUniqueIdentifierFactory<ULID>
}