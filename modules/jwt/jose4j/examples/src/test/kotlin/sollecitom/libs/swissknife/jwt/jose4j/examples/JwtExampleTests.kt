package sollecitom.libs.swissknife.jwt.jose4j.examples

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.json.test.utils.containsSameEntriesAs
import sollecitom.libs.swissknife.jwt.domain.JwtContentEncryptionAlgorithm
import sollecitom.libs.swissknife.jwt.domain.RSA.Variant.RSA_256
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.jwt.jose4j.processor.newAudienceSpecificJwtProcessor
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessor
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessorConfiguration
import sollecitom.libs.swissknife.jwt.jose4j.utils.expiryTime
import sollecitom.libs.swissknife.jwt.jose4j.utils.issuingTime
import sollecitom.libs.swissknife.jwt.jose4j.utils.notBeforeTime
import sollecitom.libs.swissknife.jwt.test.utils.jwtClaimsJson
import sollecitom.libs.swissknife.jwt.test.utils.newRandomED25519JwtIssuer
import sollecitom.libs.swissknife.jwt.test.utils.newRandomRSAJwtIssuer
import sollecitom.libs.swissknife.jwt.test.utils.newX25519JwtAudience
import sollecitom.libs.swissknife.kotlin.extensions.time.truncatedToSeconds
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logging.standard.configuration.configureLogging
import sollecitom.libs.swissknife.test.utils.assertions.containsSameElementsAs
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.InvalidJwtException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.Base64
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
class JwtExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Test
    fun `Ed25519 EdDSA JWT issuance and verification`() {

        val issuerKeyId = "issuer key"
        val issuer = newRandomED25519JwtIssuer(issuerKeyId)
        val audienceId = "audience".let(::StringOrURI)
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val jwtId = newId.ulid.monotonic().stringValue
        val subject = "subject"
        val issuingTime = now()
        val expiryTime = now() + 30.minutes
        val notBeforeTime = issuingTime - 5.seconds
        val rolesClaim = "roles"
        val roles = mutableListOf("role-1", "role-2")
        val claimsJson = jwtClaimsJson {
            it.jwtId = jwtId
            it.issuer = issuer.id.value
            it.setAudience(audienceId.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }

        val issuedJwt = issuer.issueJwt(claimsJson)
        val processedJwt = processor.readAndVerify(issuedJwt)

        assertThat(processedJwt.id).isEqualTo(jwtId)
        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerId).isEqualTo(issuer.id)
        assertThat(processedJwt.audienceIds).containsOnly(audienceId)
        assertThat(processedJwt.issuedAt).isEqualTo(issuingTime.truncatedToSeconds())
        assertThat(processedJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(processedJwt.notBeforeTime).isEqualTo(notBeforeTime.truncatedToSeconds())
        assertThat(processedJwt.getStringListClaimValue(rolesClaim)).containsSameElementsAs(roles)
        assertThat(processedJwt.claimsAsJson).containsSameEntriesAs(claimsJson)
    }

    @Test
    fun `RSA JWT issuance and verification`() {

        val issuerKeyId = "issuer key"
        val issuer = newRandomRSAJwtIssuer(variant = RSA_256, keyId = issuerKeyId)
        val audienceId = "audience".let(::StringOrURI)
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val jwtId = newId.ulid.monotonic().stringValue
        val subject = "subject"
        val issuingTime = now()
        val expiryTime = now() + 30.minutes
        val notBeforeTime = issuingTime - 5.seconds
        val rolesClaim = "roles"
        val roles = mutableListOf("role-1", "role-2")
        val claimsJson = jwtClaimsJson {
            it.jwtId = jwtId
            it.issuer = issuer.id.value
            it.setAudience(audienceId.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }

        val issuedJwt = issuer.issueJwt(claimsJson)
        val processedJwt = processor.readAndVerify(issuedJwt)

        assertThat(processedJwt.id).isEqualTo(jwtId)
        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerId).isEqualTo(issuer.id)
        assertThat(processedJwt.audienceIds).containsOnly(audienceId)
        assertThat(processedJwt.issuedAt).isEqualTo(issuingTime.truncatedToSeconds())
        assertThat(processedJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(processedJwt.notBeforeTime).isEqualTo(notBeforeTime.truncatedToSeconds())
        assertThat(processedJwt.getStringListClaimValue(rolesClaim)).containsSameElementsAs(roles)
        assertThat(processedJwt.claimsAsJson).containsSameEntriesAs(claimsJson)
    }

    @Test
    fun `RFC8037 Ed25519 EdDSA and X25519 ECDH JWT issuance and verification`() {

        val issuerKeyId = "issuer key"
        val issuer = newRandomED25519JwtIssuer(issuerKeyId)
        val audienceKeyId = "audience key"
        val audience = newX25519JwtAudience(audienceKeyId)
        val processor = newAudienceSpecificJwtProcessor(audience, issuer.id, issuer.publicKey, acceptableContentEncryptionAlgorithms = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512))
        val jwtId = newId.ulid.monotonic().stringValue
        val subject = "subject"
        val issuingTime = now()
        val expiryTime = now() + 30.minutes
        val notBeforeTime = issuingTime - 5.seconds
        val rolesClaim = "roles"
        val roles = mutableListOf("role-1", "role-2")
        val claimsJson = jwtClaimsJson {
            it.jwtId = jwtId
            it.issuer = issuer.id.value
            it.setAudience(audience.id.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }

        val issuedEncryptedJwt = issuer.issueEncryptedJwt(claimsJson, audience, contentEncryptionAlgorithm = JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)
        val processedJwt = processor.readAndVerify(issuedEncryptedJwt)

        assertThat(processedJwt.id).isEqualTo(jwtId)
        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerId).isEqualTo(issuer.id)
        assertThat(processedJwt.audienceIds).containsOnly(audience.id)
        assertThat(processedJwt.issuedAt).isEqualTo(issuingTime.truncatedToSeconds())
        assertThat(processedJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(processedJwt.notBeforeTime).isEqualTo(notBeforeTime.truncatedToSeconds())
        assertThat(processedJwt.getStringListClaimValue(rolesClaim)).containsSameElementsAs(roles)
        assertThat(processedJwt.claimsAsJson).containsSameEntriesAs(claimsJson)
    }

    @Test
    fun `a token without issuer, subject, ID and issued-at time can be read without verifying`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val expiryTime = now() + 30.minutes
        val issuedJwt = issuer.issueJwt(jwtClaimsJson { it.expiryTime = expiryTime })

        val readJwt = processor.readWithoutVerifying(issuedJwt)

        assertThat(readJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(readJwt.notBeforeTime).isNull()
    }

    @Test
    fun `a token without an issued-at time is accepted when the configuration does not require one`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val processor = newJwtProcessor(issuer.id, issuer.publicKey, newJwtProcessorConfiguration(requireIssuedAt = false))
        val subject = "subject"
        val issuedJwt = issuer.issueJwt(jwtClaimsJson {
            it.issuer = issuer.id.value
            it.subject = subject
            it.expiryTime = now() + 30.minutes
        })

        val processedJwt = processor.readAndVerify(issuedJwt)

        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerId).isEqualTo(issuer.id)
    }

    @Test
    fun `an expired token is rejected`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val issuedJwt = issuer.issueJwt(validClaims(issuer.id.value) { it.expiryTime = now() - 5.minutes })

        assertThrows<InvalidJwtException> { processor.readAndVerify(issuedJwt) }
    }

    @Test
    fun `a token signed with another key is rejected`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val impostor = newRandomED25519JwtIssuer("impostor key", issuer.id)
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val issuedJwt = impostor.issueJwt(validClaims(issuer.id.value))

        assertThrows<InvalidJwtException> { processor.readAndVerify(issuedJwt) }
    }

    @Test
    fun `a token from another issuer is rejected`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val issuedJwt = issuer.issueJwt(validClaims("another issuer"))

        assertThrows<InvalidJwtException> { processor.readAndVerify(issuedJwt) }
    }

    @Test
    fun `a token with a tampered payload is rejected`() {

        val issuer = newRandomED25519JwtIssuer("issuer key")
        val processor = newJwtProcessor(issuer.id, issuer.publicKey)
        val (header, _, signature) = issuer.issueJwt(validClaims(issuer.id.value)).split(".")
        val forgedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(validClaims(issuer.id.value) { it.subject = "admin" }.toString().toByteArray())

        assertThrows<InvalidJwtException> { processor.readAndVerify("$header.$forgedPayload.$signature") }
    }

    private fun validClaims(issuerId: String, customize: (JwtClaims) -> Unit = {}) = jwtClaimsJson {
        it.issuer = issuerId
        it.subject = "subject"
        it.issuingTime = now()
        it.expiryTime = now() + 30.minutes
        customize(it)
    }
}
