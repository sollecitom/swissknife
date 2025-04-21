package sollecitom.libs.swissknife.kotlin.extensions.runtime

import kotlinx.coroutines.runBlocking

fun Runtime.addShutdownHook(action: suspend () -> Unit) {

    Runtime.getRuntime().addShutdownHook(object : Thread() {

        override fun run() = runBlocking {
            action.invoke()
        }
    })
}