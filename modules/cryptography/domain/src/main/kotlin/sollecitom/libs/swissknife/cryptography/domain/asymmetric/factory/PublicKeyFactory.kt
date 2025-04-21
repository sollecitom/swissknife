package sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory

interface PublicKeyFactory<out PUBLIC : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey> {

    fun from(bytes: ByteArray): PUBLIC
}