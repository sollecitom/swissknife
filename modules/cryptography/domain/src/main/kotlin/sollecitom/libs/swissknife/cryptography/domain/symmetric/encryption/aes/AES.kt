package sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyFactory
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.EncryptionAlgorithm

object AES : EncryptionAlgorithm<AES.KeyArguments> {

    override val name = "AES"

    /**
     * @property keyLength the length in bits of the generated key material.
     *
     * The `*_XTS` variants carry two AES keys, as IEEE 1619 requires, so their key material is double length:
     * [AES_128_XTS] holds two AES-128 keys and [AES_256_XTS] two AES-256 keys. They drive
     * [sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode.XTS] only, just as the
     * single-key variants drive GCM only.
     */
    enum class Variant(val keyLength: Int, val algorithmName: String) {
        AES_128(128, "AES128"),
        AES_192(192, "AES192"),
        AES_256(256, "AES256"),
        AES_128_XTS(256, "AES128-XTS"),
        AES_256_XTS(512, "AES256-XTS");

        val isForXts: Boolean get() = this == AES_128_XTS || this == AES_256_XTS
    }

    data class KeyArguments(val variant: Variant)
}

operator fun SecretKeyFactory<AES.KeyArguments, SymmetricKey>.invoke(variant: AES.Variant) = invoke(arguments = AES.KeyArguments(variant = variant))