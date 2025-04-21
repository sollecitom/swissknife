package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptedData
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import javax.crypto.SecretKey

private class CTROperationsAdapter(private val key: SecretKey, private val keyMetadata: CryptographicKey.Metadata, private val random: SecureRandom, private val randomSeedLength: Int) : EncryptionMode.CTR.Operations {

    override fun encryptWithRandomIV(bytes: ByteArray) = encrypt(bytes = bytes, iv = newIv())

    override fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<EncryptionMode.CTR.Metadata> {

        val (_, encrypted) = BouncyCastleUtils.ctrEncrypt(key, iv, bytes)
        return EncryptedData(content = encrypted, metadata = EncryptionMode.CTR.Metadata(iv = iv, key = keyMetadata))
    }

    override fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray = BouncyCastleUtils.ctrDecrypt(key = key, iv = iv, cipherText = bytes)

    private fun newIv() = random.generateSeed(randomSeedLength)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CTROperationsAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()
}

fun EncryptionMode.CTR.Operations.Companion.create(key: SecretKey, keyMetadata: CryptographicKey.Metadata, random: SecureRandom, randomSeedLength: Int = DEFAULT_RANDOM_IV_LENGTH): EncryptionMode.CTR.Operations = CTROperationsAdapter(key, keyMetadata, random, randomSeedLength)