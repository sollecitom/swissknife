package sollecitom.libs.swissknife.jwt.domain

/** RSA algorithm constants and variant definitions for JWT signing. */
object RSA {

    const val algorithmName = "RSA"

    /** RSA signing algorithm variants with their JWA (JSON Web Algorithms) identifiers. */
    sealed class Variant(val name: String) {
        data object RSA_256 : Variant("RS256")
        data object RSA_384 : Variant("RS384")
        data object RSA_512 : Variant("RS512")
    }
}