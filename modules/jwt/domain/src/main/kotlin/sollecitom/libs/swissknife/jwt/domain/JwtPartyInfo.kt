package sollecitom.libs.swissknife.jwt.domain

import java.security.PublicKey

private data class JwtPartyInfo(override val id: StringOrURI, override val publicKey: PublicKey) : JwtParty

/** Creates a [JwtParty] with the given [id] and [publicKey]. */
operator fun JwtParty.Companion.invoke(id: StringOrURI, publicKey: PublicKey): JwtParty = JwtPartyInfo(id = id, publicKey = publicKey)