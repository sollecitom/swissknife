package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.aes

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.key.metadata
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyFactory
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.BC_PROVIDER
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.create
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

internal class AESKeyAdapter private constructor(private val keySpec: SecretKey, private val random: SecureRandom) : SymmetricKey, CryptographicKey by CryptographicKeyAdapter(keySpec) {

    private constructor(encoded: ByteArray, random: SecureRandom) : this(SecretKeySpec(encoded, AES.name), random)

    override val ctr: EncryptionMode.CTR.Operations by lazy { EncryptionMode.CTR.Operations.create(key = keySpec, keyMetadata = metadata, random = random) }

    init {
        require(algorithm == AES.name) { "Key algorithm must be ${AES.name}" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AESKeyAdapter

        return encoded.contentEquals(other.encoded)
    }

    override fun hashCode() = encoded.contentHashCode()

    override fun toString() = "JavaAESKeyAdapter(encoded=${encoded.contentToString()}, keySpec=${keySpec})"

    data class Factory(val random: SecureRandom) : SecretKeyFactory<AES.KeyArguments, SymmetricKey> {

        override fun invoke(arguments: AES.KeyArguments): SymmetricKey {

            val rawKey = BouncyCastleUtils.generateSecretKey(algorithm = AES.name, length = arguments.variant.keyLength, provider = BC_PROVIDER)
            return AESKeyAdapter(keySpec = rawKey, random = random)
        }

        override fun from(bytes: ByteArray): SymmetricKey = AESKeyAdapter(encoded = bytes, random = random)
    }
}