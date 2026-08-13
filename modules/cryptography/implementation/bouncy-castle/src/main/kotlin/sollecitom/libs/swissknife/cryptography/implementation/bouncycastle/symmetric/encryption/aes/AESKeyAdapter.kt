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

    override val ctr: EncryptionMode.CTR.Operations by lazy { EncryptionMode.CTR.Operations.create(key = singleAesKey("CTR"), keyMetadata = metadata, random = random) }

    override val gcm: EncryptionMode.GCM.Operations by lazy { EncryptionMode.GCM.Operations.create(key = singleAesKey("GCM"), keyMetadata = metadata, random = random) }

    override val xts: EncryptionMode.XTS.Operations by lazy { EncryptionMode.XTS.Operations.create(key = keySpec.encoded, keyMetadata = metadata) }

    init {
        require(algorithm == AES.name) { "Key algorithm must be ${AES.name}" }
    }

    /**
     * The single-key modes cannot run on double-length XTS material, so this reports that up front rather than
     * letting the provider fail later with an opaque key-length error.
     */
    private fun singleAesKey(mode: String): SecretKey {
        val keyLength = encoded.size * 8
        require(keyLength in SINGLE_AES_KEY_LENGTHS) { "$mode needs a single AES key, but this key holds $keyLength bits: it was generated for XTS, which uses two AES keys" }
        return keySpec
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

            val variant = arguments.variant
            // An XTS key is two AES keys, a length the AES key generator rejects, so its material is drawn directly.
            val rawKey = if (variant.isForXts) randomXtsKey(variant.keyLength) else BouncyCastleUtils.generateSecretKey(algorithm = AES.name, length = variant.keyLength, provider = BC_PROVIDER)
            return AESKeyAdapter(keySpec = rawKey, random = random)
        }

        private fun randomXtsKey(keyLength: Int): SecretKey = SecretKeySpec(ByteArray(keyLength / 8).also(random::nextBytes), AES.name)

        override fun from(bytes: ByteArray): SymmetricKey = AESKeyAdapter(encoded = bytes, random = random)
    }

    private companion object {

        val SINGLE_AES_KEY_LENGTHS = listOf(AES.Variant.AES_128, AES.Variant.AES_192, AES.Variant.AES_256).map { it.keyLength }
    }
}