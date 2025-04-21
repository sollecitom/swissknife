package sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueData
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueFactory

private class CtrAes256ProtectedValueFactory(private val lookupKeyForOwner: suspend (Id) -> SymmetricKey?) : ProtectedValueFactory<Id, EncryptionMode.CTR.Metadata> {

    override suspend fun <VALUE : Any> protectValue(value: VALUE, valueName: Name, owner: Id, serialize: (VALUE) -> ByteArray, iv: ByteArray?): ProtectedValue<VALUE, EncryptionMode.CTR.Metadata> {

        val key = lookupKeyForOwner(owner) ?: error("No encryption key found for owner $owner")
        val clearText = serialize(value)
        val encrypted = iv?.let { key.ctr.encrypt(clearText, iv) } ?: key.ctr.encryptWithRandomIV(clearText)
        return ProtectedValueData(encrypted.content, valueName, owner, encrypted.metadata)
    }

    class Accessible<ACCESS_CONTEXT : Any>(private val factory: ProtectedValueFactory<ACCESS_CONTEXT, EncryptionMode.CTR.Metadata>, private val lookupKeyForProtectedValue: suspend (ProtectedValue<*, EncryptionMode.CTR.Metadata>) -> SymmetricKey?) : ProtectedValueFactory<ACCESS_CONTEXT, EncryptionMode.CTR.Metadata> by factory, ProtectedValueFactory.Accessible<ACCESS_CONTEXT, EncryptionMode.CTR.Metadata> {

        override fun <VALUE : Any> makeAccessible(protectedValue: ProtectedValue<VALUE, EncryptionMode.CTR.Metadata>, deserialize: (ByteArray) -> VALUE): ProtectedValue.Accessible<VALUE, EncryptionMode.CTR.Metadata, ACCESS_CONTEXT> = ProtectedValueData.Accessible(protectedValue, deserialize, ::unprotect)

        private suspend fun unprotect(protectedValue: ProtectedValue<*, EncryptionMode.CTR.Metadata>): ByteArray {

            val key = lookupKeyForProtectedValue(protectedValue)?.apply { enforceCompatibleWith(protectedValue) } ?: error("No encryption key found for owner ${protectedValue.owner} and metadata ${protectedValue.metadata}")
            return key.ctr.decrypt(protectedValue.value, protectedValue.metadata.iv)
        }

        private fun SymmetricKey.enforceCompatibleWith(protectedValue: ProtectedValue<*, EncryptionMode.CTR.Metadata>) {

            check(protectedValue.metadata.key.algorithm == algorithm) { "Incompatible key algorithm. Expected $algorithm but was ${protectedValue.metadata.key.algorithm}" }
            check(protectedValue.metadata.key.format == format) { "Incompatible key format. Expected $format but was ${protectedValue.metadata.key.format}" }
            check(protectedValue.metadata.key.hash == hash) { "Incompatible key hash. Expected $hash but was ${protectedValue.metadata.key.hash}" }
        }
    }
}

fun ProtectedValueFactory.Companion.aes256WithCTR(lookupKeyForOwner: suspend (Id) -> SymmetricKey?): ProtectedValueFactory<Id, EncryptionMode.CTR.Metadata> = CtrAes256ProtectedValueFactory(lookupKeyForOwner)

fun ProtectedValueFactory<Id, EncryptionMode.CTR.Metadata>.accessible(lookupKeyForProtectedValue: suspend (ProtectedValue<*, EncryptionMode.CTR.Metadata>) -> SymmetricKey?): ProtectedValueFactory.Accessible<Id, EncryptionMode.CTR.Metadata> = CtrAes256ProtectedValueFactory.Accessible(this, lookupKeyForProtectedValue)
