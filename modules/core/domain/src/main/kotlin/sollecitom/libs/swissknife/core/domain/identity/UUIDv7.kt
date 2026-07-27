package sollecitom.libs.swissknife.core.domain.identity

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** A version 7 (time-ordered) UUID: sortable and carrying its creation [timestamp]. Create via [UUIDv7.invoke] or a [UniqueIdFactory]. */
class UUIDv7 internal constructor(private val delegate: Uuid) : SortableTimestampedUniqueIdentifier<UUIDv7> {

    override val stringValue by lazy(delegate::toString)
    override val bytesValue: ByteArray by lazy(delegate::toByteArray)
    override val timestamp: Instant by lazy { Instant.fromEpochMilliseconds(delegate.unixMillis) }

    override fun compareTo(other: UUIDv7) = delegate.compareTo(other.delegate)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UUIDv7
        return delegate == other.delegate
    }

    override fun hashCode() = delegate.hashCode()

    override fun toString() = "UUIDv7($stringValue)"

    companion object {

        operator fun invoke(stringValue: String): UUIDv7 = UUIDv7(Uuid.parse(stringValue))

        operator fun invoke(bytesValue: ByteArray): UUIDv7 = UUIDv7(Uuid.fromByteArray(bytesValue))

        /** Parses [value] as a version 7 UUID, returning `null` if it is not a valid UUID or is a different version. */
        fun parseOrNull(value: String): UUIDv7? = Uuid.parseOrNull(value)?.takeIf { it.version == 7 }?.let { UUIDv7(it) }
    }
}

/** The 4-bit version field of a UUID (high nibble of byte 6): 4 for random, 7 for time-ordered, etc. */
private val Uuid.version: Int get() = (toByteArray()[6].toInt() ushr 4) and 0xF

/** The 48-bit big-endian Unix-millisecond timestamp embedded in the leading bytes of a version 7 UUID. */
private val Uuid.unixMillis: Long
    get() = toByteArray().let { bytes ->
        var millis = 0L
        for (index in 0 until 6) {
            millis = (millis shl 8) or (bytes[index].toLong() and 0xFF)
        }
        millis
    }
