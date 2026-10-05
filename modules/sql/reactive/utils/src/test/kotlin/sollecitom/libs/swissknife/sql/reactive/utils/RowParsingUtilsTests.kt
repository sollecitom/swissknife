package sollecitom.libs.swissknife.sql.reactive.utils

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.r2dbc.spi.Readable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RowParsingUtilsTests {

    @Test
    fun `reads a boolean column`() {

        assertThat(SingleColumnReadable(true).booleanValue(COLUMN)).isTrue()
        assertThat(SingleColumnReadable(false).booleanValue(COLUMN)).isFalse()
    }

    private class SingleColumnReadable(private val value: Any) : Readable {

        override fun <T : Any?> get(index: Int, type: Class<T>): T = type.cast(value)

        override fun <T : Any?> get(name: String, type: Class<T>): T = type.cast(value.takeIf { name == COLUMN })
    }

    private companion object {
        const val COLUMN = "flag"
    }
}
