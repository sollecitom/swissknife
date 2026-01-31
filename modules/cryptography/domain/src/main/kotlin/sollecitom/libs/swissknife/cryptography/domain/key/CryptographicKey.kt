package sollecitom.libs.swissknife.cryptography.domain.key

import sollecitom.libs.swissknife.hashing.utils.Hash

interface CryptographicKey {

    val encoded: ByteArray
    val encodedAsHexString: String
    val algorithm: String
    val format: String

    /**
     * @property hash deterministic hash code for the key (i.e. not depending on JVM version, runtime, etc.), could be used for lookups in external systems
     */
    val hash: Hash

    /**
     * @property hash deterministic hash code for the key (i.e. not depending on JVM version, runtime, etc.), could be used for lookups in external systems
     */
    data class Metadata(val algorithm: String, val format: String, val hash: Hash) {

        companion object
    }
}

val CryptographicKey.metadata: CryptographicKey.Metadata get() = CryptographicKey.Metadata(algorithm = algorithm, format = format, hash = hash)