package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMAlgorithm

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments> {

    override val name = "KYBER"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        KYBER_512(512, "ML-KEM-512"),
        KYBER_768(768, "ML-KEM-768"),
        KYBER_1024(1024, "ML-KEM-1024")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<Kyber.KeyPairArguments, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey>.invoke(variant: Kyber.Variant) = invoke(arguments = Kyber.KeyPairArguments(variant = variant))