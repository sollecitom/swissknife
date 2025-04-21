package sollecitom.libs.swissknife.jwt.jose4j.processor

import sollecitom.libs.swissknife.jwt.domain.JWT
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import kotlinx.datetime.Instant
import org.jose4j.jwt.JwtClaims
import org.json.JSONObject

internal class JoseJwtAdapter(private val delegate: JwtClaims) : JWT {

    override val id: String get() = delegate.jwtId
    override val subject: String get() = delegate.subject
    override val claimsAsJson = delegate.toJson().let(::JSONObject)
    override val issuerId = delegate.issuer.let(::StringOrURI)
    override val audienceIds = delegate.audience.map(::StringOrURI)
    override val issuedAt: Instant = delegate.issuedAt.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val expirationTime: Instant? = delegate.expirationTime?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val notBeforeTime: Instant? = delegate.notBefore?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }

    override fun hasClaim(name: String): Boolean = delegate.hasClaim(name)

    override fun getStringListClaimValue(name: String): List<String> = delegate.getStringListClaimValue(name)
    override fun getStringClaimValue(name: String): String = delegate.getStringClaimValue(name)
}