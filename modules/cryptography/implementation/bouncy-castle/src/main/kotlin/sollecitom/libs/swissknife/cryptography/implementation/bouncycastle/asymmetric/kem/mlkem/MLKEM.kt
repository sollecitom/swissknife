package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.mlkem

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.KEMAlgorithm
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.KEMKeyPairFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import org.bouncycastle.jcajce.spec.MLKEMParameterSpec
import java.security.SecureRandom

object MLKEM : KEMAlgorithm<MLKEM.KeyPairArguments> {

    override val name: String get() = MLKEM.name

    override fun keyPairGenerationOperations(random: SecureRandom): sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPairGenerationOperations<MLKEM.KeyPairArguments, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> = MLKEMAlgorithmOperationCustomizer(random)
}

private class MLKEMAlgorithmOperationCustomizer(private val random: SecureRandom) : sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPairGenerationOperations<MLKEM.KeyPairArguments, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override val keyPair by lazy { KEMKeyPairFactory<MLKEM.KeyPairArguments>(MLKEM.name, random) { variant.spec } }
    override val privateKey by lazy { KEMPrivateKeyFactory(MLKEM.name, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(MLKEM.name, random) }

    private val MLKEM.Variant.spec: MLKEMParameterSpec
        get() = when (this) {
            MLKEM.Variant.ML_KEM_512 -> MLKEMParameterSpec.ml_kem_512
            MLKEM.Variant.ML_KEM_768 -> MLKEMParameterSpec.ml_kem_768
            MLKEM.Variant.ML_KEM_1024 -> MLKEMParameterSpec.ml_kem_1024
        }
}
