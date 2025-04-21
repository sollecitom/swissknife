package sollecitom.libs.swissknife.jwt.jose4j.issuer

import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.jwt.domain.JwtIssuer
import sollecitom.libs.swissknife.jwt.domain.RSA
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.kotlin.extensions.text.string
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.jose4j.jwk.OkpJwkGenerator
import org.jose4j.jwk.RsaJwkGenerator
import sollecitom.libs.swissknife.core.utils.string
import java.security.KeyPair

context(generator: RandomGenerator)
fun newED25519JwtIssuer(keyId: String, id: StringOrURI): JwtIssuer {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_ED25519)
    return ED25519JoseIssuerAdapter(keyPair, keyId, id)
}

fun newRSAJwtIssuer(keyPair: KeyPair, variant: RSA.Variant, id: StringOrURI, keyId: String): JwtIssuer = RSAJoseIssuerAdapter(keyPair, keyId, id, variant)

context(generator: RandomGenerator)
fun newRSAJwtIssuer(variant: RSA.Variant, keyId: String, id: StringOrURI): JwtIssuer {

    val keyPair = newRSAKeyPair(keyId)
    return newRSAJwtIssuer(keyPair = keyPair, variant = variant, id = id, keyId = keyId)
}

context(random: RandomGenerator)
fun newRSAKeyPair(keyId: String = random.string(wordLength = 20)): KeyPair {

    val rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048, null, random.secureRandom)
    rsaJsonWebKey.keyId = keyId
    return KeyPair(rsaJsonWebKey.getRsaPublicKey(), rsaJsonWebKey.rsaPrivateKey)
}

context(generator: RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, generator.secureRandom)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}
