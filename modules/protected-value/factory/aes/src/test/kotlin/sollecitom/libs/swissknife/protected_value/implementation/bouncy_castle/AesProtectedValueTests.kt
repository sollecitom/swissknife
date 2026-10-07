package sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.containsExactly
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.core.utils.provider
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.key.generator.CryptographicKeyGenerator
import sollecitom.libs.swissknife.cryptography.domain.key.generator.newAesKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.bouncyCastle
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueData
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueFactory
import sollecitom.libs.swissknife.protected_value.domain.loggingAccessHook
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import sollecitom.libs.swissknife.protected_value.domain.forType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AesProtectedValueTests : CoreDataGenerator by CoreDataGenerator.provider(), CryptographicKeyGenerator {

    override val cryptographicOperations = CryptographicOperations.bouncyCastle(random = secureRandom)

    @Test
    suspend fun `protected value masks the original value`() {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithGCM { key }
        val owner = newId.external()
        val originalValue = "jo.blogs@jp.com"
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner, String::toByteArray)

        assertThat(protectedValue.value).isNotEqualTo(originalValue.toByteArray())
        assertThat(protectedValue.name).isEqualTo(valueName)
        assertThat(String(protectedValue.value)).isNotEqualTo(originalValue)
        assertThat(protectedValue.toString()).doesNotContain(originalValue)
    }

    @Test
    suspend fun `value can be protected and unprotected`() {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithGCM { key }.accessible(ProtectedValue.loggingAccessHook()) { key }
        val owner = newId.external()
        val originalValue = "jo.blogs@jp.com"
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner, String::toByteArray)
        val accessibleProtectedValue = factory.makeAccessible(protectedValue, ::String)
        val retrievedValue = accessibleProtectedValue.access(owner)

        assertThat(retrievedValue).isEqualTo(originalValue)
    }

    @Test
    suspend fun `value can be protected and unprotected with a typed factory`() {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithGCM { key }.accessible(ProtectedValue.loggingAccessHook()) { key }.forType({ it.toString().toByteArray() }, { String(it).toInt() })
        val owner = newId.external()
        val originalValue = 1234
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner)
        val accessibleProtectedValue = factory.makeAccessible(protectedValue)
        val retrievedValue = accessibleProtectedValue.access(owner)

        assertThat(retrievedValue).isEqualTo(originalValue)
    }

    @Test
    suspend fun `a ciphertext moved to another owner cannot be decrypted`() {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithGCM { key }.accessible(ProtectedValue.loggingAccessHook()) { key }
        val owner = newId.external()
        val otherOwner = newId.external()
        val protectedValue = factory.protectValue("jo.blogs@jp.com", "email".let(::Name), owner, String::toByteArray)
        val moved = ProtectedValueData<String, _>(protectedValue.value, protectedValue.name, otherOwner, protectedValue.metadata)

        val result = runCatching { factory.makeAccessible(moved, ::String).access(otherOwner) }

        assertThat(result).failedThrowing<Exception>()
    }

    @Test
    suspend fun `the access hook sees every access before decryption`() {

        val key = newAesKey(variant = AES_256)
        val accesses = mutableListOf<Pair<Any, Name>>()
        val factory = ProtectedValueFactory.aes256WithGCM { key }.accessible({ context, value -> accesses += context to value.name }) { key }
        val owner = newId.external()
        val protectedValue = factory.protectValue("jo.blogs@jp.com", "email".let(::Name), owner, String::toByteArray)

        factory.makeAccessible(protectedValue, ::String).access(owner)

        assertThat(accesses).containsExactly(owner to "email".let(::Name))
    }

    @Test
    suspend fun `the access hook can refuse access by throwing`() {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithGCM { key }.accessible({ _, _ -> throw IllegalStateException("access refused") }) { key }
        val owner = newId.external()
        val protectedValue = factory.protectValue("jo.blogs@jp.com", "email".let(::Name), owner, String::toByteArray)

        val result = runCatching { factory.makeAccessible(protectedValue, ::String).access(owner) }

        assertThat(result).failedThrowing<IllegalStateException>()
    }
}
