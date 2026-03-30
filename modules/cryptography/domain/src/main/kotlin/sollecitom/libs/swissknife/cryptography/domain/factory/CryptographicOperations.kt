package sollecitom.libs.swissknife.cryptography.domain.factory

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector

/** Entry point for cryptographic operations, providing access to asymmetric and symmetric algorithm families. */
interface CryptographicOperations {

    val asymmetric: sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
    val symmetric: SymmetricAlgorithmFamilySelector

    companion object
}