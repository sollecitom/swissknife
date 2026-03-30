package sollecitom.libs.swissknife.jwt.domain

import kotlin.time.Instant
import org.json.JSONObject

/** A parsed JWT token with access to its standard claims. */
interface JWT {

    val id: String
    val subject: String
    val claimsAsJson: JSONObject
    val issuerId: StringOrURI
    val audienceIds: List<StringOrURI>
    val issuedAt: Instant
    val expirationTime: Instant?
    val notBeforeTime: Instant?

    fun hasClaim(name: String): Boolean

    fun getStringListClaimValue(name: String): List<String>

    fun getStringClaimValue(name: String): String

}

/** Checks whether this JWT is still valid at the given [time] (i.e., not expired). Returns true if there is no expiration time. */
fun JWT.isValidAtTime(time: Instant): Boolean = expirationTime.let { it == null || it >= time }
