package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.aes

import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyFactory
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SecretKeyGenerationOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.EncryptionAlgorithm
import java.security.SecureRandom

object AES : EncryptionAlgorithm<AES.KeyArguments> {

    override val name: String get() = AES.name

    override fun secretKeyGenerationOperations(random: SecureRandom): SecretKeyGenerationOperations<AES.KeyArguments, SymmetricKey> = AESAlgorithmOperationCustomizer(random)
}

private class AESAlgorithmOperationCustomizer(private val random: SecureRandom) : SecretKeyGenerationOperations<AES.KeyArguments, SymmetricKey> {

    override val key: SecretKeyFactory<AES.KeyArguments, SymmetricKey> by lazy { AESKeyAdapter.Factory(random) }
}

