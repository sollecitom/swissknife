package sollecitom.libs.swissknife.jwt.domain

object RSA {

    const val algorithmName = "RSA"

    sealed class Variant(val name: String) {
        data object RSA_256 : Variant("RS256")
        data object RSA_384 : Variant("RS384")
        data object RSA_512 : Variant("RS512")
    }
}