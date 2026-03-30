package sollecitom.libs.swissknife.logger.core

/** The core logging function that processes a [LogEntry]. */
fun interface Log : (LogEntry) -> Unit