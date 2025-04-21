package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.kem

import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS> : sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMAlgorithm<KEY_GENERATION_ARGUMENTS>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPrivateKey, sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.KEMPublicKey>