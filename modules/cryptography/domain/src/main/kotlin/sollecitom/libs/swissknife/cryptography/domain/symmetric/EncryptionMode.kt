package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey

/** Defines encryption modes (e.g. CTR, GCM, XTS) with their operations and metadata. */
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

    /**
     * Galois/Counter Mode: authenticated encryption (AEAD). Unlike [CTR], decryption fails if the ciphertext,
     * the authentication tag, or the associated data were tampered with.
     *
     * The IV must never be reused with the same key: doing so breaks both confidentiality and authenticity.
     * Prefer [Operations.encryptWithRandomIV].
     */
    object GCM {

        interface Operations {

            fun encrypt(bytes: ByteArray, iv: ByteArray, associatedData: ByteArray? = null): EncryptedData<Metadata>

            fun encryptWithRandomIV(bytes: ByteArray, associatedData: ByteArray? = null): EncryptedData<Metadata>

            /** @throws AuthenticationTagMismatch when the ciphertext, the tag, or [associatedData] don't match. */
            fun decrypt(bytes: ByteArray, iv: ByteArray, associatedData: ByteArray? = null): ByteArray

            companion object {
                const val DEFAULT_RANDOM_IV_LENGTH = 12
                const val DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS = 128
            }
        }

        /** Raised when GCM cannot authenticate the ciphertext, meaning it was tampered with or the key is wrong. */
        class AuthenticationTagMismatch(cause: Throwable? = null) : IllegalArgumentException("the GCM authentication tag does not match: the data was tampered with, or the key, IV, or associated data are wrong", cause)

        /**
         * @property associatedData authenticated but not encrypted, so it must be supplied again to decrypt.
         * It is retained here in the clear, exactly as GCM transmits it.
         */
        data class Metadata(val iv: ByteArray, val authenticationTagLengthInBits: Int, val associatedData: ByteArray?, override val key: CryptographicKey.Metadata) : EncryptionMode.Metadata {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Metadata

                if (!iv.contentEquals(other.iv)) return false
                if (authenticationTagLengthInBits != other.authenticationTagLengthInBits) return false
                if (!associatedData.contentEquals(other.associatedData)) return false
                if (key != other.key) return false

                return true
            }

            override fun hashCode(): Int {
                var result = iv.contentHashCode()
                result = 31 * result + authenticationTagLengthInBits
                result = 31 * result + associatedData.contentHashCode()
                result = 31 * result + key.hashCode()
                return result
            }

            override fun toString() = "Metadata(iv=${iv.contentToString()}, authenticationTagLengthInBits=$authenticationTagLengthInBits, associatedData=${associatedData?.contentToString()}, key=$key)"

            companion object
        }
    }

    /**
     * XEX-based tweaked-codebook mode with ciphertext stealing (IEEE Std 1619-2007), the mode used for
     * data-at-rest / sector encryption.
     *
     * An XTS key is two AES keys, so it holds double-length material: 256 bits for XTS-AES-128 and 512 bits
     * for XTS-AES-256. Use [sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256_XTS]
     * to generate one.
     *
     * XTS provides confidentiality but NOT authentication, and it is deterministic: the same plaintext,
     * key, and tweak always produce the same ciphertext. It is not a general-purpose replacement for [GCM].
     */
    object XTS {

        interface Operations {

            fun encrypt(bytes: ByteArray, tweak: ByteArray): EncryptedData<Metadata>

            /** Encrypts using the little-endian encoding of [dataUnitNumber] as the tweak, as IEEE 1619 prescribes. */
            fun encrypt(bytes: ByteArray, dataUnitNumber: Long): EncryptedData<Metadata>

            fun decrypt(bytes: ByteArray, tweak: ByteArray): ByteArray

            fun decrypt(bytes: ByteArray, dataUnitNumber: Long): ByteArray

            companion object {
                const val TWEAK_LENGTH = 16

                /** Ciphertext stealing needs at least one full block, so shorter inputs cannot be encrypted. */
                const val MINIMUM_DATA_LENGTH = 16
            }
        }

        data class Metadata(val tweak: ByteArray, override val key: CryptographicKey.Metadata) : EncryptionMode.Metadata {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Metadata

                if (!tweak.contentEquals(other.tweak)) return false
                if (key != other.key) return false

                return true
            }

            override fun hashCode(): Int {
                var result = tweak.contentHashCode()
                result = 31 * result + key.hashCode()
                return result
            }

            override fun toString() = "Metadata(tweak=${tweak.contentToString()}, key=$key)"

            companion object
        }
    }

    interface Metadata {

        val key: CryptographicKey.Metadata
    }
}

fun EncryptionMode.CTR.Operations.decrypt(data: EncryptedData<EncryptionMode.CTR.Metadata>) = decrypt(data.content, data.metadata.iv)

fun EncryptionMode.GCM.Operations.decrypt(data: EncryptedData<EncryptionMode.GCM.Metadata>) = decrypt(data.content, data.metadata.iv, data.metadata.associatedData)

fun EncryptionMode.XTS.Operations.decrypt(data: EncryptedData<EncryptionMode.XTS.Metadata>) = decrypt(data.content, data.metadata.tweak)