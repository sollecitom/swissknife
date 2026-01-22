package sollecitom.libs.swissknife.hashing.utils.blake3

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.hashing.utils.HashBinaryResult
import sollecitom.libs.swissknife.kotlin.extensions.text.strings
import kotlin.random.Random

@TestInstance(PER_CLASS)
private class Blake3HashingExampleTest {

    private val WELL_KNOWN_DIGEST = "Hello World"
    private val WELL_KNOWN_KEY_HEX = "88eeb57b3cde41e9518830e813f27c87579ba1a615a81a9a806912fe34cfdb4f"
    private val WELL_KNOWN_DIGEST_BLAKE3_256_BITS_HASH_HEX_LOWERCASE = "cd151423ff345bc125e46039b672bfae409dc853aea909aa2a0f7d76d09caf97"

    @Test
    fun `hashing the same value with the same key produces the same hash`() {

        val key = Random.nextBytes(size = 32)

        val hashFunction1 = Blake3.hashVariable(hashBytesLength = 64, key = key)
        val hashFunction2 = Blake3.hashVariable(hashBytesLength = 64, key = key)

        Random.strings(wordLengths = 4..10).take(100).forEach { word ->

            val firstHash = hashFunction1(word.toByteArray())
            val secondHash = hashFunction2(word.toByteArray())

            assertThat(firstHash).matches(secondHash)
        }
    }

    @Test
    fun `hashing the same value with a different key produces a different hash`() {

        val key1 = Random.nextBytes(size = 32)
        val key2 = Random.nextBytes(size = 32)

        val hashFunction1 = Blake3.hashVariable(hashBytesLength = 64, key = key1)
        val hashFunction2 = Blake3.hashVariable(hashBytesLength = 64, key = key2)

        Random.strings(wordLengths = 4..10).take(100).forEach { word ->

            val firstHash = hashFunction1(word.toByteArray())
            val secondHash = hashFunction2(word.toByteArray())

            assertThat(firstHash).doesNotMatch(secondHash)
        }
    }

    @Test
    fun `the hash produced matches the one the algorithm produces`() {

        val key = WELL_KNOWN_KEY_HEX.hexToByteArray()
        val hashFunction = Blake3.hashVariable(hashBytesLength = 32, key = key)
        val digest = WELL_KNOWN_DIGEST
        val expectedHashHex = WELL_KNOWN_DIGEST_BLAKE3_256_BITS_HASH_HEX_LOWERCASE
        val bytes = digest.toByteArray()

        val hash = hashFunction(bytes)

        assertThat(hash.bytes.toHexString(format = HexFormat.Default)).isEqualTo(expectedHashHex)
    }

    private fun Assert<HashBinaryResult>.matches(expected: HashBinaryResult) = given { actual ->

        assertThat(actual.bytes).isEqualTo(expected.bytes)
    }

    private fun Assert<HashBinaryResult>.doesNotMatch(expected: HashBinaryResult) = given { actual ->

        assertThat(actual.bytes).isNotEqualTo(expected.bytes)
    }
}