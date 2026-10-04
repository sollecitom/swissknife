package sollecitom.libs.swissknife.kotlin.extensions.number

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class NumberExtensionsTests {

    @Test
    fun `raising a long to a power is exact`() {

        assertThat(3L.pow(39)).isEqualTo(4052555153018976267L)
    }

    @Test
    fun `raising an int to a power that does not fit fails`() {

        val result = runCatching { 2.pow(31) }

        assertThat(result).isFailure().isInstanceOf(ArithmeticException::class)
    }
}
