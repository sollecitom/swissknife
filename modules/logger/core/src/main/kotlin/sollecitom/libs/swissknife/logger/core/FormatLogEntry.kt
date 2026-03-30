package sollecitom.libs.swissknife.logger.core

/** Transforms a [LogEntry] into a target format (e.g. String, JSON). */
fun interface FormatLogEntry<FORMAT> : (LogEntry) -> FORMAT