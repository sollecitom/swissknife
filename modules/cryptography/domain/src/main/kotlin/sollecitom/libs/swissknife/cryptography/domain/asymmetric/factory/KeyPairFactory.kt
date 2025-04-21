package sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory

interface KeyPairFactory<in ARGUMENTS, PRIVATE : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey, PUBLIC : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey> {

    operator fun invoke(arguments: ARGUMENTS): sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricKeyPair<PRIVATE, PUBLIC>

    fun from(privateKey: PRIVATE, publicKey: PUBLIC): sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricKeyPair<PRIVATE, PUBLIC>
}