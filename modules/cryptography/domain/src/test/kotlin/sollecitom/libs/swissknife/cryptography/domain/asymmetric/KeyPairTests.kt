package sollecitom.libs.swissknife.cryptography.domain.asymmetric

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import sollecitom.libs.swissknife.hashing.utils.Hash
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class KeyPairTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating a key pair with matching algorithms`() {

            val privateKey = testPrivateKey(algorithm = "RSA")
            val publicKey = testPublicKey(algorithm = "RSA")

            val keyPair = KeyPair(private = privateKey, public = publicKey)

            assertThat(keyPair.private).isEqualTo(privateKey)
            assertThat(keyPair.public).isEqualTo(publicKey)
        }

        @Test
        fun `creating a key pair with mismatching algorithms fails`() {

            val privateKey = testPrivateKey(algorithm = "RSA")
            val publicKey = testPublicKey(algorithm = "EC")

            val result = runCatching { KeyPair(private = privateKey, public = publicKey) }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }
    }

    private fun testPrivateKey(algorithm: String) = object : PrivateKey {
        override val encoded = byteArrayOf(1, 2, 3)
        override val encodedAsHexString = "010203"
        override val algorithm = algorithm
        override val format = "PKCS#8"
        override val hash = object : Hash {
            override val bytes = byteArrayOf(10, 20, 30)
        }
    }

    private fun testPublicKey(algorithm: String) = object : PublicKey {
        override val encoded = byteArrayOf(4, 5, 6)
        override val encodedAsHexString = "040506"
        override val algorithm = algorithm
        override val format = "X.509"
        override val hash = object : Hash {
            override val bytes = byteArrayOf(40, 50, 60)
        }
    }
}
