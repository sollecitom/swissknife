package sollecitom.libs.swissknife.logger.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext

suspend fun <T> withCoroutineLoggingContext(map: Map<String, String>, body: suspend CoroutineScope.() -> T): T = withContext(MDCContext(map)) {
    body()
}