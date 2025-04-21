package sollecitom.libs.swissknife.cryptography.domain.symmetric.algorithm

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> : sollecitom.libs.swissknife.cryptography.domain.algorithm.Algorithm