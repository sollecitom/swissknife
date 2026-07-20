package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.mldsa

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPairGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningAlgorithm
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningKeyPairFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningPrivateKeyFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.VerifyingPublicKeyFactory
import org.bouncycastle.jcajce.spec.MLDSAParameterSpec
import java.security.SecureRandom

object MLDSA : SigningAlgorithm<MLDSA.KeyPairArguments> {

    override val name: String get() = MLDSA.name

    override fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<MLDSA.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> = MLDSAAlgorithmOperationCustomizer(random)
}

private class MLDSAAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<MLDSA.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override val keyPair by lazy { SigningKeyPairFactory<MLDSA.KeyPairArguments>(MLDSA.name, random) { variant.spec } }
    override val privateKey by lazy { SigningPrivateKeyFactory(MLDSA.name, random) }
    override val publicKey by lazy { VerifyingPublicKeyFactory(MLDSA.name, random) }

    private val MLDSA.Variant.spec: MLDSAParameterSpec
        get() = when (this) {
            MLDSA.Variant.ML_DSA_44 -> MLDSAParameterSpec.ml_dsa_44
            MLDSA.Variant.ML_DSA_65 -> MLDSAParameterSpec.ml_dsa_65
            MLDSA.Variant.ML_DSA_87 -> MLDSAParameterSpec.ml_dsa_87
        }
}
