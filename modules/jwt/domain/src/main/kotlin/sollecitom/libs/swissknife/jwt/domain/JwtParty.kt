package sollecitom.libs.swissknife.jwt.domain

import java.security.PublicKey

/** A party (issuer or audience) in the JWT ecosystem, identified by an ID and a public key. */
interface JwtParty {

    val id: StringOrURI
    val publicKey: PublicKey

    companion object
}