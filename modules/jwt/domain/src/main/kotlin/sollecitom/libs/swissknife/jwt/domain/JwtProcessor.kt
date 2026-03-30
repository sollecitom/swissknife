package sollecitom.libs.swissknife.jwt.domain

/** Reads and optionally verifies JWT tokens. */
interface JwtProcessor {

    /** Parses and verifies the signature and claims of the given JWT string. */
    fun readAndVerify(jwt: String): JWT

    /** Parses the JWT string without verifying signature or claims. Use only when verification is handled elsewhere. */
    fun readWithoutVerifying(jwt: String): JWT

    /** Configuration controlling JWT verification behavior. */
    data class Configuration(
        val requireSubject: Boolean,
        val requireIssuedAt: Boolean,
        val requireExpirationTime: Boolean,
        val maximumFutureValidityInMinutes: Int?,
        val acceptableSignatureAlgorithms: Set<String>,
        val acceptableEncryptionKeyEstablishmentAlgorithms: Set<String>,
        val acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm>
    )

    companion object
}