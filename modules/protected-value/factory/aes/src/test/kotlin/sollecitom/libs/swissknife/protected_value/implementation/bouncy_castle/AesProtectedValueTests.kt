package sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.core.utils.provider
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.key.generator.CryptographicKeyGenerator
import sollecitom.libs.swissknife.cryptography.domain.key.generator.newAesKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.bouncyCastle
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueFactory
import sollecitom.libs.swissknife.protected_value.domain.forType
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AesProtectedValueTests : CoreDataGenerator by CoreDataGenerator.provider(), CryptographicKeyGenerator {

    override val cryptographicOperations = CryptographicOperations.bouncyCastle(random = secureRandom)

    @Test
    fun `protected value masks the original value`() = test {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithCTR { key }
        val owner = newId.external()
        val originalValue = "jo.blogs@jp.com"
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner, String::toByteArray)

        assertThat(protectedValue.value).isNotEqualTo(originalValue)
        assertThat(protectedValue.name).isEqualTo(valueName)
        assertThat(String(protectedValue.value)).isNotEqualTo(originalValue)
        assertThat(protectedValue.toString()).doesNotContain(originalValue)
    }

    @Test
    fun `value can be protected and unprotected`() = test {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithCTR { key }.accessible { key }
        val owner = newId.external()
        val originalValue = "jo.blogs@jp.com"
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner, String::toByteArray)
        val accessibleProtectedValue = factory.makeAccessible(protectedValue, ::String)
        val retrievedValue = accessibleProtectedValue.access(owner)

        assertThat(retrievedValue).isEqualTo(originalValue)
    }

    @Test
    fun `value can be protected and unprotected with a typed factory`() = test {

        val key = newAesKey(variant = AES_256)
        val factory = ProtectedValueFactory.aes256WithCTR { key }.accessible { key }.forType({ it.toString().toByteArray() }, { String(it).toInt() })
        val owner = newId.external()
        val originalValue = 1234
        val valueName = "email".let(::Name)

        val protectedValue = factory.protectValue(originalValue, valueName, owner)
        val accessibleProtectedValue = factory.makeAccessible(protectedValue)
        val retrievedValue = accessibleProtectedValue.access(owner)

        assertThat(retrievedValue).isEqualTo(originalValue)
    }
}