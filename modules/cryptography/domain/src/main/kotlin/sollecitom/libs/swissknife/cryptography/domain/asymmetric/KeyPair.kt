package sollecitom.libs.swissknife.cryptography.domain.asymmetric

/** A validated asymmetric key pair. Both keys must use the same algorithm. */
data class KeyPair<out PRIVATE : PrivateKey, out PUBLIC : PublicKey>(override val private: PRIVATE, override val public: PUBLIC) : AsymmetricKeyPair<PRIVATE, PUBLIC> {

    init {
        require(public.algorithm == private.algorithm) { "Public and private key must have the same algorithm" }
    }
}