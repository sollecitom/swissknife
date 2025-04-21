package sollecitom.libs.swissknife.jwt.test.utils

import sollecitom.libs.swissknife.core.utils.RandomGenerator
import org.jose4j.jwk.OkpJwkGenerator
import java.security.KeyPair

context(generator: RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, generator.secureRandom)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}