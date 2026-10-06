package sollecitom.libs.swissknife.kotlin.extensions.flows

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.milliseconds

@TestInstance(PER_CLASS)
class FlowChunkingExtensionsTests {

    @Test
    suspend fun `chunking by size emits each chunk with its own elements`() {

        val chunks = flowOf(1, 2, 3, 4).chunk(2).toList()

        assertThat(chunks).isEqualTo(listOf(listOf(1, 2), listOf(3, 4)))
    }

    @Test
    suspend fun `collecting a chunked flow again does not emit into the earlier collector`() {

        val chunked = flowOf(1, 2, 3).chunk(2)
        val firstChunks = chunked.toList()

        chunked.toList()

        assertThat(firstChunks).isEqualTo(listOf(listOf(1, 2), listOf(3)))
    }

    @Test
    fun `chunking by time emits the elements received in each period`() = runTest {

        val elements = flow {
            emit(1)
            delay(150.milliseconds)
            emit(2)
            emit(3)
            delay(100.milliseconds)
            emit(4)
        }

        val chunks = elements.chunk(maxChunkingPeriod = 100.milliseconds).toList()

        assertThat(chunks).isEqualTo(listOf(listOf(1), listOf(2, 3), listOf(4)))
    }

    @Test
    fun `chunking by size or time flushes on whichever comes first`() = runTest {

        val elements = flow {
            emit(1)
            emit(2)
            emit(3)
            delay(150.milliseconds)
            emit(4)
        }

        val chunks = elements.chunk(maxChunkSize = 2, maxChunkingPeriod = 100.milliseconds).toList()

        assertThat(chunks).isEqualTo(listOf(listOf(1, 2), listOf(3), listOf(4)))
    }

    @Test
    fun `chunks flushed by time are emitted from the collecting coroutine`() = runTest {

        val elements = flow {
            emit(1)
            delay(150.milliseconds)
        }

        val chunks = flow { emitAll(elements.chunk(maxChunkingPeriod = 100.milliseconds)) }.toList()

        assertThat(chunks).isEqualTo(listOf(listOf(1), emptyList()))
    }

    @Test
    fun `an upstream failure propagates to the collector`() = runTest {

        val elements = flow {
            emit(1)
            error("Upstream failed")
        }

        val result = runCatching { elements.chunk(maxChunkSize = 2, maxChunkingPeriod = 100.milliseconds).toList() }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("Upstream failed")
    }
}
