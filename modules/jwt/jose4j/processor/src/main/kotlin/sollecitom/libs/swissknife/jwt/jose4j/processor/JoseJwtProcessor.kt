package sollecitom.libs.swissknife.jwt.jose4j.processor

import sollecitom.libs.swissknife.jwt.domain.JWT
import sollecitom.libs.swissknife.jwt.domain.JwtProcessor
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumer
import org.jose4j.jwt.consumer.JwtConsumerBuilder

class JoseJwtProcessor(private val consumer: JwtConsumer) : JwtProcessor {

    private val noChecksConsumer = JwtConsumerBuilder().setSkipAllValidators().setDisableRequireSignature().setSkipSignatureVerification().build()

    override fun readAndVerify(jwt: String) = consumer.processToClaims(jwt).toJwt()

    override fun readWithoutVerifying(jwt: String) = noChecksConsumer.processToClaims(jwt).toJwt()

    private fun JwtClaims.toJwt(): JWT = JoseJwtAdapter(this)
}