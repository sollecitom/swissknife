package sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueData
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueFactory

private class GcmAes256ProtectedValueFactory(private val lookupKeyForOwner: suspend (Id) -> SymmetricKey?) : ProtectedValueFactory<Id, EncryptionMode.GCM.Metadata> {

    override suspend fun <VALUE : Any> protectValue(value: VALUE, valueName: Name, owner: Id, serialize: (VALUE) -> ByteArray): ProtectedValue<VALUE, EncryptionMode.GCM.Metadata> {

        val key = lookupKeyForOwner(owner) ?: error("No encryption key found for owner $owner")
        val clearText = serialize(value)
        val encrypted = key.gcm.encryptWithRandomIV(clearText, associatedData = owner.associatedData)
        return ProtectedValueData(encrypted.content, valueName, owner, encrypted.metadata)
    }

    class Accessible<ACCESS_CONTEXT : Any>(private val factory: ProtectedValueFactory<ACCESS_CONTEXT, EncryptionMode.GCM.Metadata>, private val lookupKeyForProtectedValue: suspend (ProtectedValue<*, EncryptionMode.GCM.Metadata>) -> SymmetricKey?, private val accessHook: ProtectedValue.AccessHook<ACCESS_CONTEXT, EncryptionMode.GCM.Metadata>) : ProtectedValueFactory<ACCESS_CONTEXT, EncryptionMode.GCM.Metadata> by factory, ProtectedValueFactory.Accessible<ACCESS_CONTEXT, EncryptionMode.GCM.Metadata> {

        override fun <VALUE : Any> makeAccessible(protectedValue: ProtectedValue<VALUE, EncryptionMode.GCM.Metadata>, deserialize: (ByteArray) -> VALUE): ProtectedValue.Accessible<VALUE, EncryptionMode.GCM.Metadata, ACCESS_CONTEXT> = ProtectedValueData.Accessible(protectedValue, deserialize, accessHook, ::unprotect)

        private suspend fun unprotect(protectedValue: ProtectedValue<*, EncryptionMode.GCM.Metadata>): ByteArray {

            val key = lookupKeyForProtectedValue(protectedValue)?.apply { enforceCompatibleWith(protectedValue) } ?: error("No encryption key found for owner ${protectedValue.owner} and metadata ${protectedValue.metadata}")
            return key.gcm.decrypt(protectedValue.value, protectedValue.metadata.iv, protectedValue.owner.associatedData)
        }

        private fun SymmetricKey.enforceCompatibleWith(protectedValue: ProtectedValue<*, EncryptionMode.GCM.Metadata>) {

            check(protectedValue.metadata.key.algorithm == algorithm) { "Incompatible key algorithm. Expected $algorithm but was ${protectedValue.metadata.key.algorithm}" }
            check(protectedValue.metadata.key.format == format) { "Incompatible key format. Expected $format but was ${protectedValue.metadata.key.format}" }
            check(protectedValue.metadata.key.hash == hash) { "Incompatible key hash. Expected $hash but was ${protectedValue.metadata.key.hash}" }
        }
    }
}

private val Id.associatedData: ByteArray get() = stringValue.toByteArray()

fun ProtectedValueFactory.Companion.aes256WithGCM(lookupKeyForOwner: suspend (Id) -> SymmetricKey?): ProtectedValueFactory<Id, EncryptionMode.GCM.Metadata> = GcmAes256ProtectedValueFactory(lookupKeyForOwner)

fun ProtectedValueFactory<Id, EncryptionMode.GCM.Metadata>.accessible(accessHook: ProtectedValue.AccessHook<Id, EncryptionMode.GCM.Metadata>, lookupKeyForProtectedValue: suspend (ProtectedValue<*, EncryptionMode.GCM.Metadata>) -> SymmetricKey?): ProtectedValueFactory.Accessible<Id, EncryptionMode.GCM.Metadata> = GcmAes256ProtectedValueFactory.Accessible(this, lookupKeyForProtectedValue, accessHook)
