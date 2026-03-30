package sollecitom.libs.swissknife.jwt.domain

import java.security.PrivateKey

/** A JWT audience that can decrypt JWE tokens using its private key. */
interface JwtAudience : JwtParty {

    val keyId: String
    val privateKey: PrivateKey
}