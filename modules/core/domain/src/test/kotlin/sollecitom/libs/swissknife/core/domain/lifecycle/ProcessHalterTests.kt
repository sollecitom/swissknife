package sollecitom.libs.swissknife.core.domain.lifecycle

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@TestInstance(PER_CLASS)
class ProcessHalterTests {

    @Test
    fun `halting exits with a non-zero status`() {

        val exitStatus = CompletableFuture<Int>()
        val halter = ProcessHalter(exit = { exitStatus.complete(it) }, forceHalt = {}, shutdownGracePeriod = 1.minutes)

        halter.halt(IllegalStateException("Corrupted data"))

        assertThat(exitStatus.get(5, SECONDS)).isEqualTo(1)
    }

    @Test
    fun `halting exits from a thread other than the caller's`() {

        val exitingThread = CompletableFuture<Thread>()
        val halter = ProcessHalter(exit = { exitingThread.complete(Thread.currentThread()) }, forceHalt = {}, shutdownGracePeriod = 1.minutes)

        halter.halt(IllegalStateException("Corrupted data"))

        assertThat(exitingThread.get(5, SECONDS)).isNotEqualTo(Thread.currentThread())
    }

    @Test
    fun `halting forces termination when the orderly exit doesn't complete within the grace period`() {

        val forcedStatus = CompletableFuture<Int>()
        val hungShutdown = CountDownLatch(1)
        val halter = ProcessHalter(exit = { hungShutdown.await() }, forceHalt = { forcedStatus.complete(it) }, shutdownGracePeriod = 50.milliseconds)

        halter.halt(IllegalStateException("Corrupted data"))
        val status = forcedStatus.get(5, SECONDS)
        hungShutdown.countDown()

        assertThat(status).isEqualTo(1)
    }

    @Test
    fun `halting doesn't force termination before the grace period elapses`() {

        val forced = CompletableFuture<Int>()
        val exited = CompletableFuture<Int>()
        val halter = ProcessHalter(exit = { exited.complete(it) }, forceHalt = { forced.complete(it) }, shutdownGracePeriod = 1.minutes)

        halter.halt(IllegalStateException("Corrupted data"))
        exited.get(5, SECONDS)

        assertThat(forced.isDone).isFalse()
    }
}
