package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningAlgorithm
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey

object MLDSA : SigningAlgorithm<MLDSA.KeyPairArguments> {

    override val name = "ML-DSA"

    enum class Variant(val value: String) {

        ML_DSA_44("ML-DSA-44"),
        ML_DSA_65("ML-DSA-65"),
        ML_DSA_87("ML-DSA-87")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<MLDSA.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>.invoke(variant: MLDSA.Variant) = invoke(arguments = MLDSA.KeyPairArguments(variant = variant))
