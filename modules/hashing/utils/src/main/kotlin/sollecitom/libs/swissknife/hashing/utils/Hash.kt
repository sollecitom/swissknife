package sollecitom.libs.swissknife.hashing.utils

import java.math.BigInteger

/** The result of a hash computation, represented as raw bytes. */
interface Hash {

    val bytes: ByteArray

    companion object
}

/** The length of the hash in bytes. */
val Hash.length: Int get() = bytes.size

/** Interprets the hash bytes as a [BigInteger]. */
val Hash.asBigInteger: BigInteger get() = BigInteger(bytes)

/** Computes the hash value modulo [value], useful for consistent hashing / partitioning. */
fun Hash.mod(value: BigInteger): BigInteger = asBigInteger.mod(value)