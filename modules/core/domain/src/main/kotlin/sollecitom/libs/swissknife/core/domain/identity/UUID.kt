package sollecitom.libs.swissknife.core.domain.identity

import kotlin.uuid.Uuid

/** Wrapper around [kotlin.uuid.Uuid] that implements the [Id] contract. */
class UUID internal constructor(private val delegate: Uuid) : Id {

    override val stringValue by lazy(delegate::toString)
    override val bytesValue: ByteArray by lazy { stringValue.toByteArray() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UUID
        return delegate == other.delegate
    }

    override fun hashCode() = delegate.hashCode()

    override fun toString() = "UUID($stringValue)"

    companion object {

        operator fun invoke(stringValue: String): UUID = UUID(Uuid.parse(stringValue))

        operator fun invoke(bytesValue: ByteArray): UUID = invoke(String(bytesValue))

        /** Parses [value] as a UUID in either hex-and-dash or plain hexadecimal format, returning `null` if it is not valid. */
        fun parseOrNull(value: String): UUID? = Uuid.parseOrNull(value)?.let { UUID(it) }

        /** Parses [value] as a UUID in hex-and-dash format only (e.g. `550e8400-e29b-41d4-a716-446655440000`), returning `null` otherwise. */
        fun parseHexDashOrNull(value: String): UUID? = Uuid.parseHexDashOrNull(value)?.let { UUID(it) }

        /** Parses [value] as a UUID in plain hexadecimal format only (32 hex digits, no dashes), returning `null` otherwise. */
        fun parseHexOrNull(value: String): UUID? = Uuid.parseHexOrNull(value)?.let { UUID(it) }
    }
}
