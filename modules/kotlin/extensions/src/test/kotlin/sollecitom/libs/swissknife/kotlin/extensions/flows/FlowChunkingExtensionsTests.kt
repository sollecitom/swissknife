package sollecitom.libs.swissknife.kotlin.extensions.flows

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.execution.utils.test

@TestInstance(PER_CLASS)
class FlowChunkingExtensionsTests {

    @Test
    fun `chunking by size emits each chunk with its own elements`() = test {

        val chunks = flowOf(1, 2, 3, 4).chunk(2).toList()

        assertThat(chunks).isEqualTo(listOf(listOf(1, 2), listOf(3, 4)))
    }

    @Test
    fun `collecting a chunked flow again does not emit into the earlier collector`() = test {

        val chunked = flowOf(1, 2, 3).chunk(2)
        val firstChunks = chunked.toList()

        chunked.toList()

        assertThat(firstChunks).isEqualTo(listOf(listOf(1, 2), listOf(3)))
    }
}
