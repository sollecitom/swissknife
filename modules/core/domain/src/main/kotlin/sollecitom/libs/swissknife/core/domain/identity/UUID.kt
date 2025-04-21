package sollecitom.libs.swissknife.core.domain.identity

import java.util.UUID as JavaUUID

class UUID internal constructor(private val delegate: JavaUUID) : Id {

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

        operator fun invoke(stringValue: String): UUID = UUID(JavaUUID.fromString(stringValue))

        operator fun invoke(bytesValue: ByteArray): UUID = invoke(String(bytesValue))
    }
}

