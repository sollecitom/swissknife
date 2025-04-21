package sollecitom.libs.swissknife.cryptography.domain.factory

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector

interface CryptographicOperations {

    val asymmetric: sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
    val symmetric: SymmetricAlgorithmFamilySelector

    companion object
}