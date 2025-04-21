package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.BC_PROVIDER
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaVerifyingPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : VerifyingPublicKey, CryptographicKey by CryptographicKeyAdapter(key) {

    override fun verify(input: ByteArray, signatureBytes: ByteArray, signatureAlgorithm: String): Boolean {

        return BouncyCastleUtils.verifySignature(publicKey = key, message = input, signature = signatureBytes, signatureAlgorithm = signatureAlgorithm, provider = BC_PROVIDER)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaVerifyingPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun from(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaVerifyingPublicKeyAdapter = BouncyCastleUtils.getPublicKeyFromEncoded(bytes, algorithm).let { JavaVerifyingPublicKeyAdapter(it, random) }
    }
}