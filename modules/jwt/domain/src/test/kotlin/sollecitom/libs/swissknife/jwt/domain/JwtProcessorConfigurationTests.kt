package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class JwtProcessorConfigurationTests {

    @Test
    fun `creating a configuration with all fields`() {

        val configuration = JwtProcessor.Configuration(
            requireSubject = true,
            requireIssuedAt = true,
            requireExpirationTime = false,
            maximumFutureValidityInMinutes = 60,
            acceptableSignatureAlgorithms = setOf("RS256"),
            acceptableEncryptionKeyEstablishmentAlgorithms = setOf("RSA-OAEP"),
            acceptableContentEncryptionAlgorithms = setOf(JwtContentEncryptionAlgorithm.AES_256_GCM)
        )

        assertThat(configuration.requireSubject).isTrue()
        assertThat(configuration.maximumFutureValidityInMinutes).isEqualTo(60)
        assertThat(configuration.acceptableSignatureAlgorithms).isEqualTo(setOf("RS256"))
    }

    @Test
    fun `creating a configuration with null maximum future validity`() {

        val configuration = JwtProcessor.Configuration(
            requireSubject = false,
            requireIssuedAt = false,
            requireExpirationTime = false,
            maximumFutureValidityInMinutes = null,
            acceptableSignatureAlgorithms = emptySet(),
            acceptableEncryptionKeyEstablishmentAlgorithms = emptySet(),
            acceptableContentEncryptionAlgorithms = emptySet()
        )

        assertThat(configuration.maximumFutureValidityInMinutes).isNull()
    }
}
