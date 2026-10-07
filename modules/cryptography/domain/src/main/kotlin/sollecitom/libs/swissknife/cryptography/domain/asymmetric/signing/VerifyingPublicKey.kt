package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing

import java.nio.charset.Charset

/** A public key capable of verifying digital signatures. */
interface VerifyingPublicKey : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey {

    fun verify(input: ByteArray, signatureBytes: ByteArray): Boolean
}

fun VerifyingPublicKey.verify(input: String, signatureBytes: ByteArray, charset: Charset = Charsets.UTF_8): Boolean = verify(input.toByteArray(charset), signatureBytes)

fun VerifyingPublicKey.verify(input: String, signature: Signature, charset: Charset = Charsets.UTF_8): Boolean = verify(input.toByteArray(charset), signature)

fun VerifyingPublicKey.verify(input: ByteArray, signature: Signature): Boolean = signature.metadata.algorithmName == algorithm && verify(input, signature.bytes)
