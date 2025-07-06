package sollecitom.libs.swissknife.core.domain.identity.factory.ksuid

import kotlin.time.Clock
import kotlin.random.Random

internal class KsuidVariantSelectorAdapter(random: Random, clock: Clock) : KsuidVariantSelector {

    override val monotonic = MonotonicKsuidFactoryAdapter(random, clock)
    override val withSubSecondPrecision = SubSecondPrecisionKsuidFactoryAdapter(random, clock)
}