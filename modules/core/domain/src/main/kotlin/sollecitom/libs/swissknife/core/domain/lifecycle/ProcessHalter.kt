package sollecitom.libs.swissknife.core.domain.lifecycle

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class ProcessHalter(private val exit: (status: Int) -> Unit = ::exitProcess) {

    fun halt(reason: Throwable) {

        logger.error(error = reason) { "Halting the process: ${reason.message}" }
        thread(name = "process-halter") { exit(HALTED_STATUS) }
    }

    companion object : Loggable() {
        const val HALTED_STATUS = 1
        val system = ProcessHalter()
    }
}
