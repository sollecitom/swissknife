package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import java.security.SecureRandom

internal class KEMPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.PrivateKeyFactory<sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey> {

    override fun from(bytes: ByteArray): sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey = JavaKEMPrivateKeyAdapter.from(bytes, algorithm, random)
}