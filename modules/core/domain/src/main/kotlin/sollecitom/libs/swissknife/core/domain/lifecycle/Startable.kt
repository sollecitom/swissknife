package sollecitom.libs.swissknife.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

/** A component with an async lifecycle start phase. */
interface Startable {

    suspend fun start() {}
}

/** Starts this component blocking the current thread. */
fun Startable.startBlocking() {

    runBlocking { start() }
}