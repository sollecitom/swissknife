package sollecitom.libs.swissknife.kotlin.extensions.encoding

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class Base64EncodingTests {

    @Test
    fun `encoding and decoding bytes in Base64`() {

        val bytes = "hello puppy".toByteArray()

        val encoded = bytes.base64Encoded()
        val decoded = encoded.base64Decoded()

        assertThat(decoded).isEqualTo(bytes)
    }

    @Test
    fun `encoding and decoding strings in Base64`() {

        val string = "hello puppy"

        val encoded = string.base64Encoded()
        val decoded = encoded.base64Decoded()

        assertThat(decoded).isEqualTo(string)
    }
}