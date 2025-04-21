package sollecitom.libs.swissknife.core.domain.identity.factory.ksuid

import sollecitom.libs.swissknife.core.domain.identity.KSUID
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import com.github.f4b6a3.ksuid.KsuidFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class MonotonicKsuidFactoryAdapter(random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<KSUID> {

    private val delegate: KsuidFactory = KsuidFactory.newMonotonicInstance(random.asJavaRandom(), clock.now()::toJavaInstant)

    override fun invoke() = delegate.create().let(::KSUID)

    override fun invoke(timestamp: Instant) = delegate.create(timestamp.toJavaInstant()).let(::KSUID)
}