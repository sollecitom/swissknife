package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory

interface UuidVariantSelector {

    val random: UniqueIdentifierFactory<UUID>

    /** Random (version 4) UUIDs — the same generator as [random], named explicitly by version. */
    val v4: UniqueIdentifierFactory<UUID> get() = random

    /** Time-ordered (version 7) UUIDs — sortable and timestamped. */
    val v7: SortableTimestampedUniqueIdentifierFactory<UUIDv7>
}
