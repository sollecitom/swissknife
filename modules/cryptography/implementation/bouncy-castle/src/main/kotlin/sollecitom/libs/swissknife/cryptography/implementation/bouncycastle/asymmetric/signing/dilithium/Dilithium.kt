package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPairGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium.Dilithium
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningAlgorithm
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningKeyPairFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.SigningPrivateKeyFactory
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing.VerifyingPublicKeyFactory
import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import java.security.SecureRandom

object Dilithium : SigningAlgorithm<Dilithium.KeyPairArguments> {

    override val name: String get() = Dilithium.name

    override fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> = DilithiumAlgorithmOperationCustomizer(random)
}

private class DilithiumAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override val keyPair by lazy { SigningKeyPairFactory<Dilithium.KeyPairArguments>(Dilithium.name, random) { variant.spec } }
    override val privateKey by lazy { SigningPrivateKeyFactory(Dilithium.name, random) }
    override val publicKey by lazy { VerifyingPublicKeyFactory(Dilithium.name, random) }

    private val Dilithium.Variant.spec: DilithiumParameterSpec
        get() = when (this) {
            Dilithium.Variant.DILITHIUM_2 -> DilithiumParameterSpec.dilithium2
            Dilithium.Variant.DILITHIUM_3 -> DilithiumParameterSpec.dilithium3
            Dilithium.Variant.DILITHIUM_5 -> DilithiumParameterSpec.dilithium5
        }
}

