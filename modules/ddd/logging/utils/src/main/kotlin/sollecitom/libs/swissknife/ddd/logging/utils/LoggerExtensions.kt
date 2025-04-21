package sollecitom.libs.swissknife.ddd.logging.utils

import sollecitom.libs.swissknife.correlation.logging.utils.log
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.logger.core.Logger

context(context: Event.Context)
fun Logger.log(error: Throwable? = null, evaluateMessage: () -> String) = with(context.invocation) { log(error, evaluateMessage) }