package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import sollecitom.libs.swissknife.hashing.utils.Hash
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class SignatureTests {

    @Test
    fun `signatures with same bytes and metadata are equal`() {

        val bytes = byteArrayOf(1, 2, 3)
        val metadata = Signature.Metadata(keyHash = testHash(10, 20), algorithmName = "RSA")

        val signature1 = Signature(bytes = bytes.copyOf(), metadata = metadata)
        val signature2 = Signature(bytes = bytes.copyOf(), metadata = metadata)

        assertThat(signature1).isEqualTo(signature2)
    }

    @Test
    fun `signatures with different bytes are not equal`() {

        val metadata = Signature.Metadata(keyHash = testHash(10, 20), algorithmName = "RSA")

        val signature1 = Signature(bytes = byteArrayOf(1, 2, 3), metadata = metadata)
        val signature2 = Signature(bytes = byteArrayOf(4, 5, 6), metadata = metadata)

        assertThat(signature1).isNotEqualTo(signature2)
    }

    @Test
    fun `signatures with different metadata are not equal`() {

        val bytes = byteArrayOf(1, 2, 3)
        val metadata1 = Signature.Metadata(keyHash = testHash(10, 20), algorithmName = "RSA")
        val metadata2 = Signature.Metadata(keyHash = testHash(30, 40), algorithmName = "EC")

        val signature1 = Signature(bytes = bytes.copyOf(), metadata = metadata1)
        val signature2 = Signature(bytes = bytes.copyOf(), metadata = metadata2)

        assertThat(signature1).isNotEqualTo(signature2)
    }

    @Test
    fun `hashCode is consistent for equal signatures`() {

        val bytes = byteArrayOf(1, 2, 3)
        val metadata = Signature.Metadata(keyHash = testHash(10, 20), algorithmName = "RSA")

        val signature1 = Signature(bytes = bytes.copyOf(), metadata = metadata)
        val signature2 = Signature(bytes = bytes.copyOf(), metadata = metadata)

        assertThat(signature1.hashCode()).isEqualTo(signature2.hashCode())
    }

    private fun testHash(vararg values: Byte) = object : Hash {
        override val bytes = values
    }
}
