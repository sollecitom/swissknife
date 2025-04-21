package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec

internal class KEMKeyPairFactory<ARGUMENTS>(private val algorithm: String, private val random: SecureRandom, private val spec: ARGUMENTS.() -> AlgorithmParameterSpec) : sollecitom.libs.swissknife.cryptography.domain.asymmetric.factory.KeyPairFactory<ARGUMENTS, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override fun invoke(arguments: ARGUMENTS): sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricKeyPair<sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> = arguments.generateRawKeyPair().asKEMKeyPair(random)

    override fun from(privateKey: sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, publicKey: sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey): sollecitom.libs.swissknife.cryptography.domain.asymmetric.AsymmetricKeyPair<sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey> {

        require(privateKey.algorithm == algorithm) { "Private key algorithm must be $algorithm" }
        require(publicKey.algorithm == algorithm) { "Public key algorithm must be $algorithm" }
        return sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPair(privateKey, publicKey)
    }

    private fun ARGUMENTS.generateRawKeyPair(): KeyPair = BouncyCastleUtils.generateKeyPair(algorithm, spec(), random)

    private fun KeyPair.asKEMKeyPair(random: SecureRandom) = sollecitom.libs.swissknife.cryptography.domain.asymmetric.KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun PrivateKey.asKEMPrivateKey(random: SecureRandom): sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)

    private fun PublicKey.asKEMPublicKey(random: SecureRandom): sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}