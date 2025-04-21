package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AESKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import java.security.PrivateKey as JavaPrivateKey

internal data class JavaKEMPrivateKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : KEMPrivateKey, CryptographicKey by CryptographicKeyAdapter(key) {

    private val keyFactory = AESKeyAdapter.Factory(random)

    override fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey {

        val rawEncodedSymmetricKey = BouncyCastleUtils.decryptEncapsulatedAESKey(key, encapsulatedKey, algorithm, random)
        return keyFactory.from(rawEncodedSymmetricKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun from(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPrivateKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaKEMPrivateKeyAdapter(it, random) }
    }
}