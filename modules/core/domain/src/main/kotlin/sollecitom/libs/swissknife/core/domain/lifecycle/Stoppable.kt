package sollecitom.libs.swissknife.core.domain.lifecycle

import sollecitom.libs.swissknife.kotlin.extensions.runtime.addShutdownHook
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlinx.coroutines.runBlocking

/** A component with an async lifecycle stop phase. */
interface Stoppable {

    suspend fun stop() {}
}

/** Stops this component blocking the current thread. */
fun Stoppable.stopBlocking() {

    runBlocking { stop() }
}

/** Registers a JVM shutdown hook that calls [stop] on this component. Returns the receiver for chaining. */
fun <S : Stoppable> S.stopOnJvmShutdown(): S = apply {
    Runtime.getRuntime().addShutdownHook {
        StoppableRuntimeHookManager.logger.info { "JVM graceful shutdown hook invoked by the orchestrator" }
        stop()
    }
}

private object StoppableRuntimeHookManager : Loggable()