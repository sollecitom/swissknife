package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing

import java.nio.charset.Charset

interface SigningPrivateKey : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey {

    fun sign(input: ByteArray): Signature
}

fun <OPTIONS> SigningPrivateKey.sign(input: String, charset: Charset = Charsets.UTF_8) = sign(input.toByteArray(charset))