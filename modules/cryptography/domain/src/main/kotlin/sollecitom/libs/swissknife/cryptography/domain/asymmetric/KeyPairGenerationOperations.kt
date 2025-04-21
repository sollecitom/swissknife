package sollecitom.libs.swissknife.cryptography.domain.asymmetric

interface KeyPairGenerationOperations<ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    val keyPair: sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
    val privateKey: sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.PrivateKeyFactory<PRIVATE_KEY>
    val publicKey: sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.PublicKeyFactory<PUBLIC_KEY>
}