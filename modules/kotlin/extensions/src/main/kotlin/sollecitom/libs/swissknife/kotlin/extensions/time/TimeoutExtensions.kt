package sollecitom.libs.swissknife.kotlin.extensions.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration

/** Runs [action] with the given [timeout], or without a timeout if null. */
suspend fun <T> withTimeout(timeout: Duration?, action: suspend CoroutineScope.() -> T): T = timeout?.let { kotlinx.coroutines.withTimeout(it, action) } ?: coroutineScope(action)