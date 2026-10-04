package sollecitom.libs.swissknife.kotlin.extensions.collections

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ListExtensionsTests {

    @Test
    fun `every iteration of a circular sequence starts from the first element`() {

        val sequence = listOf("a", "b", "c").circularSequence()
        sequence.take(2).toList()

        val elements = sequence.take(2).toList()

        assertThat(elements).isEqualTo(listOf("a", "b"))
    }

    @Test
    fun `the circular sequence of an empty list is empty`() {

        val elements = emptyList<String>().circularSequence().toList()

        assertThat(elements).isEmpty()
    }
}
