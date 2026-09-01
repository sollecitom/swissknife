package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey

/**
 * A symmetric encryption key that provides per-mode encryption/decryption operations.
 *
 * Not every key can drive every mode: [xts] needs two AES keys, while [gcm] needs a single one. A key
 * that cannot support a mode is rejected when that mode is used, not when the key is created, so the modes a
 * key does support stay usable.
 *
 * Key lengths overlap, so this cannot be enforced perfectly: 32 bytes is both a single AES-256 key and two
 * AES-128 keys. Generate keys with the variant matching their intended mode to keep those purposes separate.
 */
interface SymmetricKey : CryptographicKey {

    val gcm: EncryptionMode.GCM.Operations

    val xts: EncryptionMode.XTS.Operations
}