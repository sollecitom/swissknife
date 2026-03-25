package sollecitom.libs.swissknife.correlation.logging.utils

import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.logger.core.withCoroutineLoggingContext

fun InvocationContext<*>.toLoggingContext(convert: (InvocationContext<*>) -> String): Map<String, String> = mapOf("invocation" to convert(this@toLoggingContext))

suspend inline fun <ACCESS : Access, T> withLoggingContext(invocationContext: InvocationContext<ACCESS>, noinline convert: (InvocationContext<*>) -> String, crossinline action: suspend context(InvocationContext<*>) () -> T): T = withCoroutineLoggingContext(invocationContext.toLoggingContext(convert)) { action(invocationContext) }