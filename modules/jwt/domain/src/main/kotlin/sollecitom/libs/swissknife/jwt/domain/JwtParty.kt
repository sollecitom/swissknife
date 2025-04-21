package sollecitom.libs.swissknife.jwt.domain

import java.security.PublicKey

interface JwtParty {

    val id: StringOrURI
    val publicKey: PublicKey

    companion object
}