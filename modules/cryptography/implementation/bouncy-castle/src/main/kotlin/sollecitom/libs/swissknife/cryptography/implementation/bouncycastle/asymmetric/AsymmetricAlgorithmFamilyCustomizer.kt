package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.NistAlgorithmSelector
import java.security.SecureRandom

internal class AsymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : AsymmetricAlgorithmFamilySelector {

    override val nist: NistAlgorithmSelector by lazy { NistAlgorithmCustomizer(random) }
}