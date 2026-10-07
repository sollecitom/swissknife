package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class JwtValidityTests {

    private val now = Instant.parse("2026-01-01T12:00:00Z")

    @Test
    fun `a token is not valid before its not-before time`() {

        val token = jwt(notBeforeTime = now + 1.minutes, expirationTime = now + 10.minutes)

        assertThat(token.isValidAtTime(now)).isFalse()
        assertThat(token.isNotExpiredAt(now)).isTrue()
    }

    @Test
    fun `a token is valid between its not-before and expiration times`() {

        val token = jwt(notBeforeTime = now - 1.minutes, expirationTime = now + 10.minutes)

        assertThat(token.isValidAtTime(now)).isTrue()
    }

    @Test
    fun `a token is expired at its expiration time`() {

        val token = jwt(notBeforeTime = null, expirationTime = now)

        assertThat(token.isNotExpiredAt(now)).isFalse()
        assertThat(token.isValidAtTime(now)).isFalse()
    }

    @Test
    fun `a token without validity times is always valid`() {

        val token = jwt(notBeforeTime = null, expirationTime = null)

        assertThat(token.isValidAtTime(now)).isTrue()
    }

    private fun jwt(notBeforeTime: Instant?, expirationTime: Instant?): JWT = object : JWT {
        override val id: String? = null
        override val subject: String? = null
        override val claimsAsJson = JSONObject()
        override val issuerId: StringOrURI? = null
        override val audienceIds = emptyList<StringOrURI>()
        override val issuedAt: Instant? = null
        override val expirationTime = expirationTime
        override val notBeforeTime = notBeforeTime
        override fun hasClaim(name: String) = false
        override fun getStringListClaimValue(name: String) = emptyList<String>()
        override fun getStringClaimValue(name: String) = error("no claims")
    }
}
