package sollecitom.libs.swissknife.kotlin.extensions.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration

suspend fun <T> withTimeout(timeout: Duration?, action: suspend CoroutineScope.() -> T): T = timeout?.let { kotlinx.coroutines.withTimeout(it, action) } ?: coroutineScope(action)