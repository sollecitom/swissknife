package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey

/** A symmetric encryption key that provides CTR-mode encryption/decryption operations. */
interface SymmetricKey : CryptographicKey {

    val ctr: EncryptionMode.CTR.Operations
}