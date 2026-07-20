package sollecitom.libs.swissknife.cryptography.domain.asymmetric

import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.SigningPrivateKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA

interface NistAlgorithmSelector {

    val mlKem: KeyPairGenerationOperations<MLKEM.KeyPairArguments, KEMPrivateKey, KEMPublicKey>
    val mlDsa: KeyPairGenerationOperations<MLDSA.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>
}
