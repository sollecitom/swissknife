package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AESKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaKEMPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : KEMPublicKey, CryptographicKey by CryptographicKeyAdapter(key) {

    private val keyFactory = AESKeyAdapter.Factory(random)

    override fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation {

        val rawKeyAndEncapsulation = BouncyCastleUtils.generateAESEncryptionKey(key, algorithm, random)
        return SymmetricKeyWithEncapsulation(key = keyFactory.from(rawKeyAndEncapsulation.encoded), encapsulation = rawKeyAndEncapsulation.encapsulation)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun from(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPublicKeyAdapter = BouncyCastleUtils.getPublicKeyFromEncoded(bytes, algorithm).let { JavaKEMPublicKeyAdapter(it, random) }
    }
}