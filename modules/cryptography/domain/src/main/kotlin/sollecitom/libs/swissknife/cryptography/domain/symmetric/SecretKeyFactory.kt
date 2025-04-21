package sollecitom.libs.swissknife.cryptography.domain.symmetric

interface SecretKeyFactory<in ARGUMENTS, out KEY : SymmetricKey> {

    operator fun invoke(arguments: ARGUMENTS): KEY

    fun from(bytes: ByteArray): KEY
}