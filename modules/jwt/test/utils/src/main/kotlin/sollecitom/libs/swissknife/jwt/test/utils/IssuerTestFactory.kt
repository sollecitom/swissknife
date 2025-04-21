package sollecitom.libs.swissknife.jwt.test.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.jwt.domain.RSA
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.jwt.jose4j.issuer.newED25519JwtIssuer
import sollecitom.libs.swissknife.jwt.jose4j.issuer.newRSAJwtIssuer
import sollecitom.libs.swissknife.kotlin.extensions.text.string
import java.net.URI

context(RandomGenerator)
fun newRandomED25519JwtIssuer(keyId: String = random.string(wordLength = 6), id: StringOrURI) = newED25519JwtIssuer(keyId, id)

context(RandomGenerator)
fun newRandomED25519JwtIssuer(keyId: String = random.string(wordLength = 6), id: String = Name.random().value) = newED25519JwtIssuer(keyId, StringOrURI(id))

context(RandomGenerator)
fun newRandomED25519JwtIssuer(keyId: String = random.string(wordLength = 6), id: URI) = newED25519JwtIssuer(keyId, StringOrURI(id))

context(RandomGenerator)
fun newRandomRSAJwtIssuer(variant: RSA.Variant, keyId: String = random.string(wordLength = 6), id: StringOrURI) = newRSAJwtIssuer(variant = variant, keyId = keyId, id = id)

context(RandomGenerator)
fun newRandomRSAJwtIssuer(variant: RSA.Variant, keyId: String = random.string(wordLength = 6), id: String = Name.random().value) = newRSAJwtIssuer(variant = variant, keyId = keyId, id = StringOrURI(id))

context(RandomGenerator)
fun newRandomRSAJwtIssuer(variant: RSA.Variant, keyId: String = random.string(wordLength = 6), id: URI) = newRSAJwtIssuer(variant = variant, keyId = keyId, id = StringOrURI(id))