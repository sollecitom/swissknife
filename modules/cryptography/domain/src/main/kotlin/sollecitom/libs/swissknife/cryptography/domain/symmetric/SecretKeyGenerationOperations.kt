package sollecitom.libs.swissknife.cryptography.domain.symmetric

interface SecretKeyGenerationOperations<ARGUMENTS, PRIVATE_KEY : SymmetricKey> {

    val key: SecretKeyFactory<ARGUMENTS, PRIVATE_KEY>
}