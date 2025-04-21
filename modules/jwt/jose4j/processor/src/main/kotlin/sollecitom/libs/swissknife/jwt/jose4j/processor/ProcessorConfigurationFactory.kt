package sollecitom.libs.swissknife.jwt.jose4j.processor

import sollecitom.libs.swissknife.jwt.domain.JwtContentEncryptionAlgorithm
import sollecitom.libs.swissknife.jwt.domain.JwtProcessor
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jws.AlgorithmIdentifiers

fun newJwtProcessorConfiguration(
    requireSubject: Boolean = true,
    requireIssuedAt: Boolean = true,
    requireExpirationTime: Boolean = true,
    maximumFutureValidityInMinutes: Int? = null,
    acceptableSignatureAlgorithms: Set<String> = setOf(AlgorithmIdentifiers.EDDSA, AlgorithmIdentifiers.RSA_USING_SHA256, AlgorithmIdentifiers.RSA_USING_SHA384, AlgorithmIdentifiers.RSA_USING_SHA512),
    acceptableEncryptionKeyEstablishmentAlgorithms: Set<String> = setOf(KeyManagementAlgorithmIdentifiers.ECDH_ES, KeyManagementAlgorithmIdentifiers.DIRECT),
    acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm> = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)
) = JwtProcessor.Configuration(requireSubject, requireIssuedAt, requireExpirationTime, maximumFutureValidityInMinutes, acceptableSignatureAlgorithms, acceptableEncryptionKeyEstablishmentAlgorithms, acceptableContentEncryptionAlgorithms)