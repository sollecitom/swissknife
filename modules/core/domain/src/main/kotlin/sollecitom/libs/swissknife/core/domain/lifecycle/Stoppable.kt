package sollecitom.libs.swissknife.core.domain.lifecycle

import sollecitom.libs.swissknife.kotlin.extensions.runtime.addShutdownHook
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlinx.coroutines.runBlocking

interface Stoppable {

    suspend fun stop() {}
}

fun Stoppable.stopBlocking() {

    runBlocking { stop() }
}

fun <S : Stoppable> S.stopOnJvmShutdown(): S = apply {
    Runtime.getRuntime().addShutdownHook {
        StoppableRuntimeHookManager.logger.info { "JVM graceful shutdown hook invoked by the orchestrator" }
        stop()
    }
}

private object StoppableRuntimeHookManager : Loggable()