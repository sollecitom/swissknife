package sollecitom.libs.swissknife.kotlin.extensions.bytes

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class BytesExtensionsTests {

    @Test
    fun `zero requires no bits`() {

        val result = 0.requiredBits

        assertThat(result).isEqualTo(0)
    }
}
