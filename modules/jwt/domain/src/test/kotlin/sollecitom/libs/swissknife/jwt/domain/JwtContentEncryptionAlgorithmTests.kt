package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class JwtContentEncryptionAlgorithmTests {

    @Test
    fun `AES 128 CBC HMAC SHA 256 has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_128_CBC_HMAC_SHA_256.value).isEqualTo("A128CBC-HS256")
    }

    @Test
    fun `AES 192 CBC HMAC SHA 384 has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_192_CBC_HMAC_SHA_384.value).isEqualTo("A192CBC-HS384")
    }

    @Test
    fun `AES 256 CBC HMAC SHA 512 has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512.value).isEqualTo("A256CBC-HS512")
    }

    @Test
    fun `AES 128 GCM has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_128_GCM.value).isEqualTo("A128GCM")
    }

    @Test
    fun `AES 192 GCM has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_192_GCM.value).isEqualTo("A192GCM")
    }

    @Test
    fun `AES 256 GCM has the correct value`() {

        assertThat(JwtContentEncryptionAlgorithm.AES_256_GCM.value).isEqualTo("A256GCM")
    }

    @Test
    fun `all algorithms have non-empty values`() {

        JwtContentEncryptionAlgorithm.entries.forEach { algorithm ->
            assertThat(algorithm.value).isNotEmpty()
        }
    }
}
