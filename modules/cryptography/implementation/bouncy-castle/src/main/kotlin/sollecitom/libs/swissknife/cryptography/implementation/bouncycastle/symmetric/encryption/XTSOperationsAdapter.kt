package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptedData
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.xts.XtsAes

private class XTSOperationsAdapter(private val key: ByteArray, private val keyMetadata: CryptographicKey.Metadata) : EncryptionMode.XTS.Operations {

    override fun encrypt(bytes: ByteArray, tweak: ByteArray): EncryptedData<EncryptionMode.XTS.Metadata> {

        val encrypted = XtsAes.encrypt(key = key, tweak = tweak, data = bytes)
        return EncryptedData(content = encrypted, metadata = EncryptionMode.XTS.Metadata(tweak = tweak, key = keyMetadata))
    }

    override fun encrypt(bytes: ByteArray, dataUnitNumber: Long) = encrypt(bytes = bytes, tweak = XtsAes.tweakOf(dataUnitNumber))

    override fun decrypt(bytes: ByteArray, tweak: ByteArray): ByteArray = XtsAes.decrypt(key = key, tweak = tweak, data = bytes)

    override fun decrypt(bytes: ByteArray, dataUnitNumber: Long) = decrypt(bytes = bytes, tweak = XtsAes.tweakOf(dataUnitNumber))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XTSOperationsAdapter

        return key.contentEquals(other.key)
    }

    override fun hashCode() = key.contentHashCode()
}

fun EncryptionMode.XTS.Operations.Companion.create(key: ByteArray, keyMetadata: CryptographicKey.Metadata): EncryptionMode.XTS.Operations = XTSOperationsAdapter(key, keyMetadata)
