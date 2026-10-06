package sollecitom.libs.swissknife.logger.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

suspend fun <T> withCoroutineLoggingContext(map: Map<String, String>, body: suspend CoroutineScope.() -> T): T = withContext(MDCContext(map)) {
    body()
}

/** Runs the blocking [body] with [map] added to the current thread's logging context, restoring the previous context afterwards. */
fun <T> withThreadLoggingContext(map: Map<String, String>, body: () -> T): T {

    val previous: Map<String, String>? = MDC.getCopyOfContextMap()
    MDC.setContextMap(previous.orEmpty() + map)
    try {
        return body()
    } finally {
        if (previous == null) MDC.clear() else MDC.setContextMap(previous)
    }
}
