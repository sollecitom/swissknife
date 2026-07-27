package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory

internal class UuidVariantSelectorAdapter : UuidVariantSelector {

    override val random: UniqueIdentifierFactory<UUID> get() = UuidFactoryAdapter
    override val v7: SortableTimestampedUniqueIdentifierFactory<UUIDv7> get() = UuidV7FactoryAdapter
}
