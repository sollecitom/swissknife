package sollecitom.libs.swissknife.core.domain.identity.factory.ksuid

import sollecitom.libs.swissknife.core.domain.identity.KSUID
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory

interface KsuidVariantSelector {

    val monotonic: SortableTimestampedUniqueIdentifierFactory<KSUID>
    val withSubSecondPrecision: SortableTimestampedUniqueIdentifierFactory<KSUID>
}