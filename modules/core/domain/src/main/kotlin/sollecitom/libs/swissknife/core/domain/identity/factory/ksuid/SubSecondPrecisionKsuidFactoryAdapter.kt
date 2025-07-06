package sollecitom.libs.swissknife.core.domain.identity.factory.ksuid

import sollecitom.libs.swissknife.core.domain.identity.KSUID
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import com.github.f4b6a3.ksuid.KsuidFactory
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class SubSecondPrecisionKsuidFactoryAdapter(random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<KSUID> {

    private val delegate: KsuidFactory = KsuidFactory.newSubsecondInstance(random.asJavaRandom(), clock.now()::toJavaInstant)

    override fun invoke() = delegate.create().let(::KSUID)

    override fun invoke(timestamp: Instant) = delegate.create(timestamp.toJavaInstant()).let(::KSUID)
}