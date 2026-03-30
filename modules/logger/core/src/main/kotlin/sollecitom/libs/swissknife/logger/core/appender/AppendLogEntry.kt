package sollecitom.libs.swissknife.logger.core.appender

import sollecitom.libs.swissknife.logger.core.LogEntry

/** Writes a [LogEntry] to an output destination (e.g. console, file). */
fun interface AppendLogEntry : (LogEntry) -> Unit