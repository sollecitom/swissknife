package sollecitom.libs.swissknife.jwt.domain

import kotlinx.datetime.Instant
import org.json.JSONObject

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

fun JWT.isValidAtTime(time: Instant): Boolean = expirationTime.let { it == null || it >= time }
