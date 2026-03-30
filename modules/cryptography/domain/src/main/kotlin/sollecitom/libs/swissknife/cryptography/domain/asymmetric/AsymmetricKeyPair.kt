package sollecitom.libs.swissknife.cryptography.domain.asymmetric

/** A pair of matching asymmetric keys (private and public). */
interface AsymmetricKeyPair<out PRIVATE : PrivateKey, out PUBLIC : PublicKey> {

    val private: PRIVATE
    val public: PUBLIC
}