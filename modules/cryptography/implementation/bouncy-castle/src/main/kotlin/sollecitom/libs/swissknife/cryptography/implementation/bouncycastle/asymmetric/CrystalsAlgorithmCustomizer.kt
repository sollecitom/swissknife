package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.Kyber
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium.Dilithium
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber by lazy { Kyber.keyPairGenerationOperations(random) }
    override val dilithium by lazy { Dilithium.keyPairGenerationOperations(random) }
}