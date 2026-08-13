package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptedData
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import javax.crypto.AEADBadTagException
import javax.crypto.SecretKey

private class GCMOperationsAdapter(private val key: SecretKey, private val keyMetadata: CryptographicKey.Metadata, private val random: SecureRandom, private val randomIvLength: Int, private val authenticationTagLengthInBits: Int) : EncryptionMode.GCM.Operations {

    override fun encryptWithRandomIV(bytes: ByteArray, associatedData: ByteArray?) = encrypt(bytes = bytes, iv = newIv(), associatedData = associatedData)

    override fun encrypt(bytes: ByteArray, iv: ByteArray, associatedData: ByteArray?): EncryptedData<EncryptionMode.GCM.Metadata> {

        val encrypted = BouncyCastleUtils.gcmEncrypt(key = key, iv = iv, data = bytes, associatedData = associatedData, tagLengthInBits = authenticationTagLengthInBits)
        val metadata = EncryptionMode.GCM.Metadata(iv = iv, authenticationTagLengthInBits = authenticationTagLengthInBits, associatedData = associatedData, key = keyMetadata)
        return EncryptedData(content = encrypted, metadata = metadata)
    }

    override fun decrypt(bytes: ByteArray, iv: ByteArray, associatedData: ByteArray?): ByteArray = try {
        BouncyCastleUtils.gcmDecrypt(key = key, iv = iv, cipherText = bytes, associatedData = associatedData, tagLengthInBits = authenticationTagLengthInBits)
    } catch (e: AEADBadTagException) {
        throw EncryptionMode.GCM.AuthenticationTagMismatch(e)
    }

    // nextBytes, not generateSeed: IVs are generated per message, and seed generation can block on the entropy source.
    private fun newIv() = ByteArray(randomIvLength).also(random::nextBytes)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GCMOperationsAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()
}

fun EncryptionMode.GCM.Operations.Companion.create(key: SecretKey, keyMetadata: CryptographicKey.Metadata, random: SecureRandom, randomIvLength: Int = DEFAULT_RANDOM_IV_LENGTH, authenticationTagLengthInBits: Int = DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS): EncryptionMode.GCM.Operations = GCMOperationsAdapter(key, keyMetadata, random, randomIvLength, authenticationTagLengthInBits)
