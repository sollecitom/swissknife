package sollecitom.libs.swissknife.correlation.logging.test.utils

import kotlinx.coroutines.CoroutineScope
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.create
import sollecitom.libs.swissknife.correlation.core.test.utils.testWithInvocationContext
import sollecitom.libs.swissknife.correlation.logging.utils.withLoggingContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

context(_: TimeGenerator, _: UniqueIdGenerator)
fun testWithInvocationLoggingContext(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 10.seconds,
    invocationContext: InvocationContext<*> = InvocationContext.create(),
    convert: (InvocationContext<*>) -> String = InvocationContext<*>::toString,
    testBody: suspend context(InvocationContext<*>) CoroutineScope.(InvocationContext<*>) -> Unit
) = testWithInvocationContext(context = context, timeout = timeout, invocationContext = invocationContext) {
    val scope = this
    withLoggingContext(invocationContext, convert) { testBody(invocationContext, scope, invocationContext) }
}
