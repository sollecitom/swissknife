package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.factory.ksuid.KsuidVariantSelectorAdapter
import sollecitom.libs.swissknife.core.domain.identity.factory.string.StringFactoryAdapter
import sollecitom.libs.swissknife.core.domain.identity.factory.ulid.UlidVariantSelectorAdapter
import sollecitom.libs.swissknife.core.domain.identity.factory.uuid.UuidVariantSelectorAdapter
import kotlinx.datetime.Clock
import kotlin.random.Random

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System) : UniqueIdFactory {

    override val ulid by lazy { UlidVariantSelectorAdapter(random, clock) }
    override val ksuid by lazy { KsuidVariantSelectorAdapter(random, clock) }
    override val uuid by lazy { UuidVariantSelectorAdapter() }
    override val internal get() = ulid.monotonic
    override val external by lazy { StringFactoryAdapter(random) { ulid.monotonic().stringValue } }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock)