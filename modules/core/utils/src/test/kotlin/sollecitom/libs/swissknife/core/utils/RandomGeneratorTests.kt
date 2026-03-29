package sollecitom.libs.swissknife.core.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isIn
import assertk.assertions.isLessThan
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.security.SecureRandom

@TestInstance(PER_CLASS)
class RandomGeneratorTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class NextIntWithRange {

        @Test
        fun `generating a random int within a range`() {

            val range = 5..10
            val result = generator.nextInt(range)

            assertThat(result).isIn(*range.toList().toTypedArray())
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NextIntWithFromUntil {

        @Test
        fun `generating a random int with from and until`() {

            val from = 0
            val until = 100
            val result = generator.nextInt(from, until)

            assertThat(result).isGreaterThanOrEqualTo(from)
            assertThat(result).isLessThan(until)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NextLongWithFromUntil {

        @Test
        fun `generating a random long with from and until`() {

            val from = 0L
            val until = 1000L
            val result = generator.nextLong(from, until)

            assertThat(result).isGreaterThanOrEqualTo(from)
            assertThat(result).isLessThan(until)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class StringGeneration {

        @Test
        fun `generating a random string with a fixed length`() {

            val length = 10
            val result = generator.string(length)

            assertThat(result.length).isEqualTo(length)
        }

        @Test
        fun `generating a random string with a length range`() {

            val lengthRange = 5..15
            val result = generator.string(lengthRange)

            assertThat(result.length).isGreaterThanOrEqualTo(lengthRange.first)
            assertThat(result.length).isIn(*lengthRange.toList().toTypedArray())
        }

        @Test
        fun `generating random strings sequence`() {

            val length = 8
            val results = generator.strings(length).take(3).toList()

            assertThat(results.size).isEqualTo(3)
            results.forEach { assertThat(it.length).isEqualTo(length) }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class SecureRandomAdapter {

        @Test
        fun `creating a RandomGenerator from a SecureRandom`() {

            val secureRandom = SecureRandom()
            val adapted = secureRandom.asRandomGenerator()

            val result = adapted.string(10)

            assertThat(result).isNotEmpty()
        }
    }

    private val secureRandom = SecureRandom()
    private val generator = secureRandom.asRandomGenerator()
}
