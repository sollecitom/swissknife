package sollecitom.libs.swissknife.cryptography.domain.key.generator

import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.invoke

interface CryptographicKeyGenerator : CryptographicOperations {

    val cryptographicOperations: CryptographicOperations

    override val asymmetric get() = cryptographicOperations.asymmetric
    override val symmetric get() = cryptographicOperations.symmetric

}

fun CryptographicKeyGenerator.newAesKey(variant: AES.Variant) = symmetric.aes.key(variant)