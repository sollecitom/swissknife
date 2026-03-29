package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RSATests {

    @Test
    fun `algorithm name is RSA`() {

        assertThat(RSA.algorithmName).isEqualTo("RSA")
    }

    @Test
    fun `RSA 256 variant has the correct name`() {

        assertThat(RSA.Variant.RSA_256.name).isEqualTo("RS256")
    }

    @Test
    fun `RSA 384 variant has the correct name`() {

        assertThat(RSA.Variant.RSA_384.name).isEqualTo("RS384")
    }

    @Test
    fun `RSA 512 variant has the correct name`() {

        assertThat(RSA.Variant.RSA_512.name).isEqualTo("RS512")
    }
}
