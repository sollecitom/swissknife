package sollecitom.libs.swissknife.core.domain.lifecycle

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlin.concurrent.thread
import kotlin.system.exitProcess
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ProcessHalter(
    private val exit: (status: Int) -> Unit = ::exitProcess,
    private val forceHalt: (status: Int) -> Unit = Runtime.getRuntime()::halt,
    private val shutdownGracePeriod: Duration = 30.seconds
) {

    fun halt(reason: Throwable) {

        logger.error(error = reason) { "Halting the process: ${reason.message}" }
        thread(name = "process-halter-deadline", isDaemon = true) {
            Thread.sleep(shutdownGracePeriod.inWholeMilliseconds)
            forceHalt(HALTED_STATUS)
        }
        thread(name = "process-halter") { exit(HALTED_STATUS) }
    }

    companion object : Loggable() {
        const val HALTED_STATUS = 1
        val system = ProcessHalter()
    }
}
