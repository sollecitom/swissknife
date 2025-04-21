package sollecitom.libs.swissknife.jwt.domain

import java.security.PrivateKey

interface JwtAudience : JwtParty {

    val keyId: String
    val privateKey: PrivateKey
}