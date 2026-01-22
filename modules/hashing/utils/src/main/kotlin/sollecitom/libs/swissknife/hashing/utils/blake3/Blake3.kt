package sollecitom.libs.swissknife.hashing.utils.blake3

import sollecitom.libs.swissknife.hashing.utils.HashFunction
import sollecitom.libs.swissknife.hashing.utils.HashVariableResult
import org.apache.commons.codec.digest.Blake3 as CommonsBlake3

object Blake3 {

    const val MIN_HASH_LENGTH_BITES = 32
    const val KEY_LENGTH_BITES = 32

    val hash256: HashFunction<HashVariableResult> by lazy { hash256() }

    fun hash256(key: ByteArray? = null): HashFunction<HashVariableResult> = hashVariable(32, key)

    fun hashVariable(hashBytesLength: Int, key: ByteArray? = null): HashFunction<HashVariableResult> = object : HashFunction<HashVariableResult> {

        override fun invoke(bytes: ByteArray, offset: Int, length: Int): HashVariableResult {
            val hashBytes = blake3(bytes, hashBytesLength, offset, hashBytesLength, key)
            return HashVariableResult.create(hashBytes)
        }
    }

    private fun blake3(bytes: ByteArray, hashBytesLength: Int = MIN_HASH_LENGTH_BITES, offset: Int = 0, length: Int = hashBytesLength, key: ByteArray? = null): ByteArray {

        require(hashBytesLength >= MIN_HASH_LENGTH_BITES) { "Minimum hash length for BLAKE3 is $MIN_HASH_LENGTH_BITES, but $hashBytesLength was requested." }
        val hasher = hasher(key)
        hasher.update(bytes)
        println("HashBytesLength: $hashBytesLength, length: $length")
        val hash = ByteArray(hashBytesLength)
        hasher.doFinalize(hash, offset, hashBytesLength)
        return hash
    }

    private fun hasher(key: ByteArray? = null): CommonsBlake3 {

        if (key != null) {
            require(key.size == KEY_LENGTH_BITES) { "Invalid key length for Blake3! Must be $KEY_LENGTH_BITES but was ${key.size}." }
        }
        return key?.let { CommonsBlake3.initKeyedHash(it) } ?: CommonsBlake3.initHash()
    }
}