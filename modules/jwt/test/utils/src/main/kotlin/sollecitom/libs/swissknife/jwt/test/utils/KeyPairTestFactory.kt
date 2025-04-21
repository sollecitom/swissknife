package sollecitom.libs.swissknife.jwt.test.utils

import sollecitom.libs.swissknife.core.utils.RandomGenerator
import org.jose4j.jwk.OkpJwkGenerator
import java.security.KeyPair

context(RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, secureRandom)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}