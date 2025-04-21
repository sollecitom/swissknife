package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AES
import java.security.SecureRandom
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES as AESAlgorithm

internal class SymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : SymmetricAlgorithmFamilySelector {

    override val aes: SecretKeyGenerationOperations<AESAlgorithm.KeyArguments, SymmetricKey> by lazy { AES.secretKeyGenerationOperations(random) }
}