package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal class UuidV7FactoryAdapter(private val random: Random, private val clock: Clock) : SortableTimestampedUniqueIdentifierFactory<UUIDv7> {

    private var lastMostSignificantBits = 0L
    private var lastLeastSignificantBits = 0L

    @Synchronized
    override fun invoke(): UUIDv7 {
        val candidate = generate(clock.now().toEpochMilliseconds())
        val next = if (candidate.isAfterLast()) candidate else incrementLast()
        lastMostSignificantBits = next.first
        lastLeastSignificantBits = next.second
        return UUIDv7(delegate = Uuid.fromLongs(next.first, next.second))
    }

    override fun invoke(timestamp: Instant): UUIDv7 = generate(timestamp.toEpochMilliseconds()).let { (most, least) -> UUIDv7(delegate = Uuid.fromLongs(most, least)) }

    private fun generate(unixMillis: Long): Pair<Long, Long> {
        val mostSignificantBits = (unixMillis shl 16) or 0x7000L or (random.nextLong() and 0x0FFFL)
        val leastSignificantBits = (random.nextLong() and 0x3FFFFFFFFFFFFFFFL) or Long.MIN_VALUE
        return mostSignificantBits to leastSignificantBits
    }

    private fun Pair<Long, Long>.isAfterLast() = first.toULong() > lastMostSignificantBits.toULong() || (first == lastMostSignificantBits && second.toULong() > lastLeastSignificantBits.toULong())

    private fun incrementLast(): Pair<Long, Long> {
        val randomBits = (lastLeastSignificantBits and 0x3FFFFFFFFFFFFFFFL) + 1
        if (randomBits <= 0x3FFFFFFFFFFFFFFFL) return lastMostSignificantBits to (randomBits or Long.MIN_VALUE)
        val counterAndTimestamp = ((lastMostSignificantBits ushr 16) shl 12 or (lastMostSignificantBits and 0x0FFFL)) + 1
        val mostSignificantBits = ((counterAndTimestamp ushr 12) shl 16) or 0x7000L or (counterAndTimestamp and 0x0FFFL)
        return mostSignificantBits to Long.MIN_VALUE
    }
}
