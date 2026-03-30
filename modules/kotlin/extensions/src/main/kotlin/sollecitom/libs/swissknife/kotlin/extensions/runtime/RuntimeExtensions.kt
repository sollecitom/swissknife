package sollecitom.libs.swissknife.kotlin.extensions.runtime

import kotlinx.coroutines.runBlocking

/** Registers a JVM shutdown hook that runs the given suspend [action] in a blocking coroutine. */
fun Runtime.addShutdownHook(action: suspend () -> Unit) {

    Runtime.getRuntime().addShutdownHook(object : Thread() {

        override fun run() = runBlocking {
            action.invoke()
        }
    })
}