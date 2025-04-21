package sollecitom.libs.swissknife.cryptography.domain.asymmetric.algorithm

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PrivateKey, PUBLIC_KEY : sollecitom.libs.swissknife.cryptography.domain.asymmetric.PublicKey> : sollecitom.libs.swissknife.cryptography.domain.algorithm.Algorithm