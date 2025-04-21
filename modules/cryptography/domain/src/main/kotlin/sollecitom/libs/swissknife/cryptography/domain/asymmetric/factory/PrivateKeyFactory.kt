package sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory

interface PrivateKeyFactory<out PRIVATE : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey> {

    fun from(bytes: ByteArray): PRIVATE
}