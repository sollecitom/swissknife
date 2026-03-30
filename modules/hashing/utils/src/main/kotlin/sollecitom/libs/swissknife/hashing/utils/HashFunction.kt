package sollecitom.libs.swissknife.hashing.utils

/** A hash function that computes a hash from a byte array (or a subrange of it). */
interface HashFunction<out RESULT : Any> {

    operator fun invoke(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size): RESULT
}

/** Convenience overload that hashes an arbitrary [value] by first converting it to bytes. */
operator fun <VALUE : Any, RESULT : Any> HashFunction<RESULT>.invoke(value: VALUE, toByteArray: VALUE.() -> ByteArray) = invoke(bytes = value.toByteArray())