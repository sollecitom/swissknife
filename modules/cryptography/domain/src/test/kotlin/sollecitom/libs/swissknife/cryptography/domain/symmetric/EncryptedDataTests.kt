package sollecitom.libs.swissknife.cryptography.domain.symmetric

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.hashing.utils.Hash
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EncryptedDataTests {

    @Test
    fun `encrypted data with same content and metadata are equal`() {

        val content = byteArrayOf(1, 2, 3)
        val metadata = testMetadata()

        val data1 = EncryptedData(content = content.copyOf(), metadata = metadata)
        val data2 = EncryptedData(content = content.copyOf(), metadata = metadata)

        assertThat(data1).isEqualTo(data2)
    }

    @Test
    fun `encrypted data with different content are not equal`() {

        val metadata = testMetadata()

        val data1 = EncryptedData(content = byteArrayOf(1, 2, 3), metadata = metadata)
        val data2 = EncryptedData(content = byteArrayOf(4, 5, 6), metadata = metadata)

        assertThat(data1).isNotEqualTo(data2)
    }

    @Test
    fun `hashCode is consistent for equal data`() {

        val content = byteArrayOf(1, 2, 3)
        val metadata = testMetadata()

        val data1 = EncryptedData(content = content.copyOf(), metadata = metadata)
        val data2 = EncryptedData(content = content.copyOf(), metadata = metadata)

        assertThat(data1.hashCode()).isEqualTo(data2.hashCode())
    }

    @Test
    fun `toString includes content and metadata`() {

        val content = byteArrayOf(1, 2, 3)
        val metadata = testMetadata()

        val data = EncryptedData(content = content, metadata = metadata)

        assertThat(data.toString()).contains("EncryptedData")
    }

    private fun testMetadata(): EncryptionMode.CTR.Metadata {
        val hash = object : Hash {
            override val bytes = byteArrayOf(10, 20)
        }
        val keyMetadata = CryptographicKey.Metadata(algorithm = "AES", format = "RAW", hash = hash)
        return EncryptionMode.CTR.Metadata(iv = byteArrayOf(99), key = keyMetadata)
    }
}
