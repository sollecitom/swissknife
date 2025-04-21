package sollecitom.libs.swissknife.cryptography.domain.symmetric

import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey

interface SymmetricKey : CryptographicKey {

    val ctr: EncryptionMode.CTR.Operations
}