package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey

interface KEMPrivateKey : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey {

    fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey
}