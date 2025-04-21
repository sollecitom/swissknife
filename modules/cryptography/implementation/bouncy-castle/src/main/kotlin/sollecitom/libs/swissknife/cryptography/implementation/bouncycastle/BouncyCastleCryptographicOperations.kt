package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithmFamilyCustomizer
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.SymmetricAlgorithmFamilyCustomizer
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import java.security.SecureRandom
import java.security.Security

private class BouncyCastleCryptographicOperations(private val random: SecureRandom) : CryptographicOperations {

    override val asymmetric: AsymmetricAlgorithmFamilySelector by lazy { AsymmetricAlgorithmFamilyCustomizer(random) }
    override val symmetric: SymmetricAlgorithmFamilySelector by lazy { SymmetricAlgorithmFamilyCustomizer(random) }

    companion object {
        init {
            if (Security.getProvider(BCPQC_PROVIDER) == null) {
                Security.addProvider(BouncyCastlePQCProvider())
            }
            if (Security.getProvider(BC_PROVIDER) == null) {
                Security.addProvider(BouncyCastleProvider())
            }
        }
    }
}

fun CryptographicOperations.Companion.bouncyCastle(random: SecureRandom = SecureRandom()): CryptographicOperations = BouncyCastleCryptographicOperations(random)