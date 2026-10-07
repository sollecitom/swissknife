package sollecitom.libs.swissknife.jwt.domain

import kotlin.time.Instant
import org.json.JSONObject

/** A parsed JWT token with access to its standard claims. */
interface JWT {

    val id: String?
    val subject: String?
    val claimsAsJson: JSONObject
    val issuerId: StringOrURI?
    val audienceIds: List<StringOrURI>
    val issuedAt: Instant?
    val expirationTime: Instant?
    val notBeforeTime: Instant?

    fun hasClaim(name: String): Boolean

    fun getStringListClaimValue(name: String): List<String>

    fun getStringClaimValue(name: String): String

}

/** Whether this JWT is not yet expired at [time] (RFC 7519: [time] is before the expiration time). True if there is no expiration time. */
fun JWT.isNotExpiredAt(time: Instant): Boolean = expirationTime.let { it == null || time < it }

/** Whether this JWT is valid at [time]: not expired, and not before its not-before time. */
fun JWT.isValidAtTime(time: Instant): Boolean = isNotExpiredAt(time) && notBeforeTime.let { it == null || time >= it }
