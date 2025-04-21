package sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.algorithm.SymmetricAlgorithm

interface EncryptionAlgorithm<KEY_GENERATION_ARGUMENTS> : SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SymmetricKey>