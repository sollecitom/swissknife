package sollecitom.libs.swissknife.logger.core.appender

import sollecitom.libs.swissknife.logger.core.LogEntry

fun interface AppendLogEntry : (LogEntry) -> Unit