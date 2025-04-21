package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.signing

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import java.security.SecureRandom

internal class SigningPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory<SigningPrivateKey> {

    override fun from(bytes: ByteArray): SigningPrivateKey = JavaSigningKeyAdapter.from(bytes, random, algorithm)
}