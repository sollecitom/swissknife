package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import java.security.SecureRandom

internal class KEMPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.PublicKeyFactory<sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override fun from(bytes: ByteArray): sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey = JavaKEMPublicKeyAdapter.from(bytes, algorithm, random)
}