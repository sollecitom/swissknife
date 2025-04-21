package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES

interface SymmetricAlgorithmFamilySelector {

    val aes: SecretKeyGenerationOperations<AES.KeyArguments, SymmetricKey>
}