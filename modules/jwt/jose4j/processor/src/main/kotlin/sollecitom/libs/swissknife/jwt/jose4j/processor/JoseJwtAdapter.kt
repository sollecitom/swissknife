package sollecitom.libs.swissknife.jwt.jose4j.processor

import sollecitom.libs.swissknife.jwt.domain.JWT
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import kotlin.time.Instant
import org.jose4j.jwt.JwtClaims
import org.json.JSONObject

internal class JoseJwtAdapter(private val delegate: JwtClaims) : JWT {

    override val id: String get() = checkNotNull(delegate.jwtId) { "The JWT has no 'jti' claim" }
    override val subject: String get() = checkNotNull(delegate.subject) { "The JWT has no 'sub' claim" }
    override val claimsAsJson = delegate.toJson().let(::JSONObject)
    override val issuerId: StringOrURI get() = checkNotNull(delegate.issuer) { "The JWT has no 'iss' claim" }.let(::StringOrURI)
    override val audienceIds = delegate.audience.map(::StringOrURI)
    override val issuedAt: Instant get() = checkNotNull(delegate.issuedAt) { "The JWT has no 'iat' claim" }.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val expirationTime: Instant? = delegate.expirationTime?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val notBeforeTime: Instant? = delegate.notBefore?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }

    override fun hasClaim(name: String): Boolean = delegate.hasClaim(name)

    override fun getStringListClaimValue(name: String): List<String> = delegate.getStringListClaimValue(name)
    override fun getStringClaimValue(name: String): String = delegate.getStringClaimValue(name)
}