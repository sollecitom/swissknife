package sollecitom.libs.swissknife.correlation.logging.utils

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.invoke
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.LoggingLevel

context(context: InvocationContext<*>)
fun Logger.log(error: Throwable? = null, evaluateMessage: () -> String) {

    log(context.logLevel, error, evaluateMessage)
}

val InvocationContext<*>.logLevel: LoggingLevel
    get() = when (Toggles.InvocationVisibility(this)) {
        null -> LoggingLevel.DEBUG
        InvocationVisibility.HIGH -> LoggingLevel.INFO
        InvocationVisibility.DEFAULT -> LoggingLevel.DEBUG
    }