package sollecitom.libs.swissknife.test.utils.execution.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun test(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 20.seconds,
    testBody: suspend CoroutineScope.() -> Unit
) {
    runBlocking(context) { withTimeout(timeout) { testBody.invoke(this) } }
}