package sollecitom.libs.swissknife.cryptography.domain.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber.Kyber
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium.Dilithium

interface CrystalsAlgorithmSelector {

    val kyber: KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>
    val dilithium: KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
}