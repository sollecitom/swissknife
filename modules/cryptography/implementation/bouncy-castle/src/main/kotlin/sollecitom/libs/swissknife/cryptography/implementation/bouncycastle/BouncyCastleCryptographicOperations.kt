package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithmFamilyCustomizer
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.SymmetricAlgorithmFamilyCustomizer
import java.security.SecureRandom

private class BouncyCastleCryptographicOperations(private val random: SecureRandom) : CryptographicOperations {

    override val asymmetric: AsymmetricAlgorithmFamilySelector by lazy { AsymmetricAlgorithmFamilyCustomizer(random) }
    override val symmetric: SymmetricAlgorithmFamilySelector by lazy { SymmetricAlgorithmFamilyCustomizer(random) }

    companion object {
        init {
            ensureBouncyCastleProviderIsRegistered()
        }
    }
}

fun CryptographicOperations.Companion.bouncyCastle(random: SecureRandom = SecureRandom()): CryptographicOperations = BouncyCastleCryptographicOperations(random)