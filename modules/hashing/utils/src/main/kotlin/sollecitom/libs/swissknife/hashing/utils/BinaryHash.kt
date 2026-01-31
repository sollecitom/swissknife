package sollecitom.libs.swissknife.hashing.utils

internal class BinaryHash internal constructor(override val bytes: ByteArray): Hash {

    override fun toString(): String = bytes.contentToString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Hash) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int = bytes.contentHashCode()

}

fun Hash.Companion.create(bytes: ByteArray): Hash = BinaryHash(bytes)