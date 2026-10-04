package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA.Variant.ML_DSA_87
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.invoke
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.invoke
import sollecitom.libs.swissknife.cryptography.test.specification.CryptographyTestSpecification
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.security.SecureRandom


@TestInstance(PER_CLASS)
class BouncyCastleCryptographyExampleTests : CryptographyTestSpecification {

    override val cryptography: CryptographicOperations get() = CryptographicOperations.bouncyCastle()

    @Test
    fun `AES key generation draws from the injected random source`() {

        val first = CryptographicOperations.bouncyCastle(random = ConstantRandom()).symmetric.aes.key(variant = AES_256)
        val second = CryptographicOperations.bouncyCastle(random = ConstantRandom()).symmetric.aes.key(variant = AES_256)

        assertThat(first.encoded).isEqualTo(second.encoded)
    }

    @Test
    fun `signing draws from the injected random source`() {

        val keyPair = CryptographicOperations.bouncyCastle(random = ConstantRandom()).asymmetric.nist.mlDsa.keyPair(variant = ML_DSA_87)
        val message = "something to attest".toByteArray()

        val first = keyPair.private.sign(message)
        val second = keyPair.private.sign(message)

        assertThat(first.bytes).isEqualTo(second.bytes)
    }

    private class ConstantRandom : SecureRandom() {

        override fun nextBytes(bytes: ByteArray) = bytes.fill(7)
    }
}
