package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import java.security.SecureRandom

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> {

    fun secretKeyGenerationOperations(random: SecureRandom): SecretKeyGenerationOperations<KEY_GENERATION_ARGUMENTS, KEY>
}