package sollecitom.libs.swissknife.jwt.jose4j.issuer

import sollecitom.libs.swissknife.jwt.domain.*
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.json.JSONObject
import java.security.KeyPair
import java.security.PublicKey

class RSAJoseIssuerAdapter(private val keyPair: KeyPair, private val keyId: String, override val id: StringOrURI, private val variant: RSA.Variant) : JwtIssuer {

    init {
        require(keyPair.public.algorithm == RSA.algorithmName) { "Public key algorithm '${keyPair.public.algorithm}' is unsupported. Public key must use an algorithm in $allowedAlgorithms." }
        require(keyPair.private.algorithm == RSA.algorithmName) { "Private key algorithm '${keyPair.private.algorithm}' is unsupported. Private key must use an algorithm in $allowedAlgorithms." }
        require(keyPair.public.algorithm == keyPair.private.algorithm) { "Private key algorithm '${keyPair.private.algorithm}' must be equal to the public key algorithm '${keyPair.public.algorithm}'." }
    }

    override val publicKey: PublicKey get() = keyPair.public

    override fun issueJwt(claims: JSONObject): String {

        val jws = JsonWebSignature()
        jws.payload = claims.toString()
        jws.key = keyPair.private
        jws.keyIdHeaderValue = keyId
        jws.algorithmHeaderValue = variant.name
        return jws.compactSerialization
    }

    override fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String {

        val jwe = JsonWebEncryption()
        jwe.algorithmHeaderValue = KeyManagementAlgorithmIdentifiers.ECDH_ES
        jwe.encryptionMethodHeaderParameter = contentEncryptionAlgorithm.value
        jwe.key = audience.publicKey
        jwe.keyIdHeaderValue = audience.keyId
        jwe.contentTypeHeaderValue = OUTER_JWT_CONTENT_TYPE_HEADER_VALUE
        jwe.payload = innerJwt
        return jwe.compactSerialization
    }

    companion object {
        private const val OUTER_JWT_CONTENT_TYPE_HEADER_VALUE = "JWT"
        private val allowedAlgorithms = setOf(RSA)
    }
}