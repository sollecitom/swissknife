package sollecitom.libs.swissknife.jwt.domain

import org.json.JSONObject

/** A JWT issuer capable of signing and optionally encrypting tokens. */
interface JwtIssuer : JwtParty {

    /** Issues a signed JWT and then encrypts it for the given [audience]. */
    fun issueEncryptedJwt(claims: JSONObject, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm = JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512): String {

        val innerJwt = issueJwt(claims)
        return encryptJwt(innerJwt, audience, contentEncryptionAlgorithm)
    }

    /** Issues a signed JWT (JWS) with the given claims. */
    fun issueJwt(claims: JSONObject): String

    /** Encrypts an already-signed JWT (JWS) for the given [audience], producing a JWE. */
    fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String
}