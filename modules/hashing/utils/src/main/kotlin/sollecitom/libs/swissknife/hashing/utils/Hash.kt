package sollecitom.libs.swissknife.hashing.utils

import java.math.BigInteger

interface Hash {

    val bytes: ByteArray

    companion object
}

val Hash.length: Int get() = bytes.size

val Hash.asBigInteger: BigInteger get() = BigInteger(bytes)

fun Hash.mod(value: BigInteger): BigInteger = asBigInteger.mod(value)