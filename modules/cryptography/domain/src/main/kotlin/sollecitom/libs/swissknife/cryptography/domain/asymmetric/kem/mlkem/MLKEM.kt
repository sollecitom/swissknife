package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMAlgorithm

object MLKEM : KEMAlgorithm<MLKEM.KeyPairArguments> {

    override val name = "ML-KEM"

    enum class Variant(val keyLength: Int, val algorithmName: String) {
        ML_KEM_512(512, "ML-KEM-512"),
        ML_KEM_768(768, "ML-KEM-768"),
        ML_KEM_1024(1024, "ML-KEM-1024")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<MLKEM.KeyPairArguments, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey>.invoke(variant: MLKEM.Variant) = invoke(arguments = MLKEM.KeyPairArguments(variant = variant))
