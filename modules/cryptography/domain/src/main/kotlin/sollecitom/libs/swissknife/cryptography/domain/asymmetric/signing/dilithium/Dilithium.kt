package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningAlgorithm
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey

object Dilithium : SigningAlgorithm<Dilithium.KeyPairArguments> {

    override val name = "Dilithium"

    enum class Variant(val value: String) {

        DILITHIUM_2("DILITHIUM2"),
        DILITHIUM_3("DILITHIUM3"),
        DILITHIUM_5("DILITHIUM5")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>.invoke(variant: Dilithium.Variant) = invoke(arguments = Dilithium.KeyPairArguments(variant = variant))