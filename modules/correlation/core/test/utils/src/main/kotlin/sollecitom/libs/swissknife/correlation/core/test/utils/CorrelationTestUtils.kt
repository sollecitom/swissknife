package sollecitom.libs.swissknife.correlation.core.test.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.create
import sollecitom.libs.swissknife.kotlin.extensions.time.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

context(_: TimeGenerator, _: UniqueIdGenerator)
fun testWithInvocationContext(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 10.seconds,
    invocationContext: InvocationContext<*> = InvocationContext.create(),
    testBody: suspend context(InvocationContext<*>) CoroutineScope.() -> Unit
) {
    runBlocking(context) { withTimeout(timeout) { testBody.invoke(invocationContext, this) } }
}