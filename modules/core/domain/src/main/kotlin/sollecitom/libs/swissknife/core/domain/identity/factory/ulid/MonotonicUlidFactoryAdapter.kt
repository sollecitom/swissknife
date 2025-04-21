package sollecitom.libs.swissknife.core.domain.identity.factory.ulid

import sollecitom.libs.swissknife.core.domain.identity.ULID
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import com.github.f4b6a3.ulid.UlidFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class MonotonicUlidFactoryAdapter(random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<ULID> {

    private val delegate = UlidFactory.newMonotonicInstance(random.asJavaRandom()) {
        clock.now().toEpochMilliseconds()
    }

    override fun invoke() = delegate.create().let(::ULID)

    override fun invoke(timestamp: Instant) = delegate.create(timestamp.toEpochMilliseconds()).let(::ULID)
}