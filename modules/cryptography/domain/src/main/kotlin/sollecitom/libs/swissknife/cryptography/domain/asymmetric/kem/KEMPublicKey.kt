package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation

interface KEMPublicKey : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey {

    fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation
}