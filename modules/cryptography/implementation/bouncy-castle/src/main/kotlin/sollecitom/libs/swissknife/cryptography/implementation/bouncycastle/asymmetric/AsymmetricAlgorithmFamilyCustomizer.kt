package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import java.security.SecureRandom

internal class AsymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : AsymmetricAlgorithmFamilySelector {

    override val crystals: CrystalsAlgorithmSelector by lazy { CrystalsAlgorithmCustomizer(random) }
}