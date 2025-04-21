package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPairGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey
import java.security.SecureRandom

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
}