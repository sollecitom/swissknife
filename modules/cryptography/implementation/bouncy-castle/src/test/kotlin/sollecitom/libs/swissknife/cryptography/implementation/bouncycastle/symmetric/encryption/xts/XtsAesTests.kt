package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.xts

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import java.util.HexFormat

/**
 * Known-answer tests for XTS-AES against the published IEEE Std 1619-2007 vectors. These pin the mode to the
 * standard, which round-trip tests alone cannot do: a self-consistent but wrong implementation round-trips fine.
 */
@TestInstance(PER_CLASS)
class XtsAesTests {

    private val hex: HexFormat = HexFormat.of()

    private fun String.decodeHex(): ByteArray = hex.parseHex(this)

    private fun ByteArray.encodeHex(): String = hex.formatHex(this)

    @Nested
    @TestInstance(PER_CLASS)
    inner class Ieee1619Vectors {

        @Test
        fun `vector 1 - XTS-AES-128 with zero keys and zero data`() {

            val key = ("00000000000000000000000000000000" + "00000000000000000000000000000000").decodeHex()
            val plaintext = "0000000000000000000000000000000000000000000000000000000000000000".decodeHex()
            val expected = "917cf69ebd68b2ec9b9fe9a3eadda692cd43d2f59598ed858c02c2652fbf922e"

            val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(0), data = plaintext)

            assertThat(encrypted.encodeHex()).isEqualTo(expected)
            assertThat(XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(0), data = encrypted)).isEqualTo(plaintext)
        }

        @Test
        fun `vector 2 - XTS-AES-128 with a non-zero data unit number`() {

            val key = ("11111111111111111111111111111111" + "22222222222222222222222222222222").decodeHex()
            val plaintext = "4444444444444444444444444444444444444444444444444444444444444444".decodeHex()
            val expected = "c454185e6a16936e39334038acef838bfb186fff7480adc4289382ecd6d394f0"

            val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(0x3333333333L), data = plaintext)

            assertThat(encrypted.encodeHex()).isEqualTo(expected)
            assertThat(XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(0x3333333333L), data = encrypted)).isEqualTo(plaintext)
        }

        @Test
        fun `vector 10 - XTS-AES-256`() {

            // The vector encrypts a 512-byte data unit. With no ciphertext stealing every block is independent,
            // so the first two blocks of that data unit are asserted here.
            val key = ("2718281828459045235360287471352662497757247093699959574966967627" + "3141592653589793238462643383279502884197169399375105820974944592").decodeHex()
            val plaintext = ByteArray(32) { index -> index.toByte() }
            val expected = "1c3b3a102f770386e4836c99e370cf9bea00803f5e482357a4ae12d414a3e63b"

            val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(0xff), data = plaintext)

            assertThat(encrypted.encodeHex()).isEqualTo(expected)
            assertThat(XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(0xff), data = encrypted)).isEqualTo(plaintext)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class CiphertextStealing {

        private val key = ("2718281828459045235360287471352662497757247093699959574966967627" + "3141592653589793238462643383279502884197169399375105820974944592").decodeHex()

        @Test
        fun `round-trips data that is not a whole number of blocks`() {

            val plaintext = ByteArray(37) { index -> index.toByte() }

            val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(255), data = plaintext)
            val decrypted = XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(255), data = encrypted)

            assertThat(decrypted).isEqualTo(plaintext)
        }

        @Test
        fun `preserves the length of the plaintext`() {

            (16..48).forEach { length ->
                val plaintext = ByteArray(length) { index -> index.toByte() }

                val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(1), data = plaintext)

                assertThat(encrypted.size).isEqualTo(length)
                assertThat(XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(1), data = encrypted)).isEqualTo(plaintext)
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Rejections {

        private val key = ("2718281828459045235360287471352662497757247093699959574966967627" + "3141592653589793238462643383279502884197169399375105820974944592").decodeHex()

        @Test
        fun `rejects data shorter than a block`() {

            assertThrows<IllegalArgumentException> { XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(0), data = ByteArray(15)) }
        }

        @Test
        fun `rejects a tweak that is not 16 bytes`() {

            assertThrows<IllegalArgumentException> { XtsAes.encrypt(key = key, tweak = ByteArray(12), data = ByteArray(32)) }
        }

        @Test
        fun `rejects a single-length AES key`() {

            // 16 bytes is one AES-128 key; XTS needs two, so 32 and 64 bytes are the only valid lengths.
            assertThrows<IllegalArgumentException> { XtsAes.encrypt(key = ByteArray(16), tweak = XtsAes.tweakOf(0), data = ByteArray(32)) }
            assertThrows<IllegalArgumentException> { XtsAes.encrypt(key = ByteArray(48), tweak = XtsAes.tweakOf(0), data = ByteArray(32)) }
        }

        @Test
        fun `accepts both XTS key lengths`() {

            listOf(32, 64).forEach { keyLength ->
                val key = ByteArray(keyLength) { index -> index.toByte() }
                val plaintext = ByteArray(32) { index -> index.toByte() }

                val encrypted = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(0), data = plaintext)

                assertThat(XtsAes.decrypt(key = key, tweak = XtsAes.tweakOf(0), data = encrypted)).isEqualTo(plaintext)
            }
        }

        @Test
        fun `rejects a negative data unit number`() {

            assertThrows<IllegalArgumentException> { XtsAes.tweakOf(-1) }
        }
    }

    @Test
    fun `a different tweak produces different ciphertext`() {

        val key = ("11111111111111111111111111111111" + "22222222222222222222222222222222").decodeHex()
        val plaintext = ByteArray(32) { index -> index.toByte() }

        val first = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(1), data = plaintext)
        val second = XtsAes.encrypt(key = key, tweak = XtsAes.tweakOf(2), data = plaintext)

        assertThat(first.encodeHex()).isNotEqualTo(second.encodeHex())
    }
}
