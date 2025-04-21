package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey

object EncryptionMode {

    object CTR {

        interface Operations {

            fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<Metadata>

            fun encryptWithRandomIV(bytes: ByteArray): EncryptedData<Metadata>

            fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray

            companion object {
                const val DEFAULT_RANDOM_IV_LENGTH = 16
            }
        }

        data class Metadata(val iv: ByteArray, override val key: CryptographicKey.Metadata) : EncryptionMode.Metadata {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Metadata

                if (!iv.contentEquals(other.iv)) return false
                if (key != other.key) return false

                return true
            }

            override fun hashCode(): Int {
                var result = iv.contentHashCode()
                result = 31 * result + key.hashCode()
                return result
            }

            override fun toString() = "Metadata(iv=${iv.contentToString()}, key=$key)"

            companion object
        }
    }

    interface Metadata {

        val key: CryptographicKey.Metadata
    }
}

fun EncryptionMode.CTR.Operations.decrypt(data: EncryptedData<EncryptionMode.CTR.Metadata>) = decrypt(data.content, data.metadata.iv)