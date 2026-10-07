package sollecitom.libs.swissknife.core.domain.lifecycle

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit.SECONDS

@TestInstance(PER_CLASS)
class ProcessHalterTests {

    @Test
    fun `halting exits with a non-zero status`() {

        val exitStatus = CompletableFuture<Int>()
        val halter = ProcessHalter(exit = { exitStatus.complete(it) })

        halter.halt(IllegalStateException("Corrupted data"))

        assertThat(exitStatus.get(5, SECONDS)).isEqualTo(1)
    }

    @Test
    fun `halting exits from a thread other than the caller's`() {

        val exitingThread = CompletableFuture<Thread>()
        val halter = ProcessHalter(exit = { exitingThread.complete(Thread.currentThread()) })

        halter.halt(IllegalStateException("Corrupted data"))

        assertThat(exitingThread.get(5, SECONDS)).isNotEqualTo(Thread.currentThread())
    }
}
