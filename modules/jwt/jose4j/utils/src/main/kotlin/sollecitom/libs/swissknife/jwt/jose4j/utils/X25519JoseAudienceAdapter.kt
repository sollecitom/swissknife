package sollecitom.libs.swissknife.jwt.jose4j.utils

import sollecitom.libs.swissknife.jwt.domain.JwtAudience
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

class X25519JoseAudienceAdapter(private val keyPair: KeyPair, override val keyId: String, override val id: StringOrURI) : JwtAudience {

    init {
        require(keyPair.public.algorithm == KEY_ALGORITHM) { "Public key must use ${OctetKeyPairJsonWebKey.SUBTYPE_X25519}" }
        require(keyPair.private.algorithm == KEY_ALGORITHM) { "Private key must use ${OctetKeyPairJsonWebKey.SUBTYPE_X25519}" }
    }

    override val publicKey: PublicKey get() = keyPair.public
    override val privateKey: PrivateKey get() = keyPair.private

    companion object {
        private const val KEY_ALGORITHM = "XDH"
    }
}