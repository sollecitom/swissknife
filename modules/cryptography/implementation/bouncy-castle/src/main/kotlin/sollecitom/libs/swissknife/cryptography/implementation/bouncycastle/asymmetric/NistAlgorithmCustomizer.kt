package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.NistAlgorithmSelector
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.mlkem.MLKEM
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.mldsa.MLDSA
import java.security.SecureRandom

internal class NistAlgorithmCustomizer(private val random: SecureRandom) : NistAlgorithmSelector {

    override val mlKem by lazy { MLKEM.keyPairGenerationOperations(random) }
    override val mlDsa by lazy { MLDSA.keyPairGenerationOperations(random) }
}
