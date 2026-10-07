package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory
import kotlin.random.Random
import kotlin.time.Clock

internal class UuidVariantSelectorAdapter(random: Random, clock: Clock) : UuidVariantSelector {

    override val random: UniqueIdentifierFactory<UUID> = UuidFactoryAdapter(random)
    override val v7: SortableTimestampedUniqueIdentifierFactory<UUIDv7> = UuidV7FactoryAdapter(random, clock)
}
