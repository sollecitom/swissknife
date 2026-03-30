package sollecitom.libs.swissknife.logger.core

/** Predicate that determines whether a [LogEntry] should be logged. */
fun interface FilterLogEntry : (LogEntry) -> Boolean