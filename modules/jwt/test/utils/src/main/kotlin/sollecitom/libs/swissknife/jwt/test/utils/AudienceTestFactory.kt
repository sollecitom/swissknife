package sollecitom.libs.swissknife.jwt.test.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.jwt.domain.JwtAudience
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.jwt.jose4j.utils.X25519JoseAudienceAdapter
import sollecitom.libs.swissknife.kotlin.extensions.text.string
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import sollecitom.libs.swissknife.core.utils.string

context(random: RandomGenerator)
fun newX25519JwtAudience(keyId: String = random.string(wordLength = 6), id: StringOrURI = Name.random().value.let(::StringOrURI)): JwtAudience {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_X25519)
    return X25519JoseAudienceAdapter(keyPair, keyId, id)
}