package sollecitom.libs.swissknife.logger.core.appender

import sollecitom.libs.swissknife.logger.core.LogEntry

/** Template for log appenders that filter, format, and then output entries. Override the three abstract methods. */
abstract class AppenderTemplate<FORMAT : Any> : AppendLogEntry {

    final override fun invoke(entry: LogEntry) {

        if (shouldLogEntry(entry)) {
            val formattedEntry = formatEntry(entry)
            appendFormattedEntry(formattedEntry)
        }
    }

    abstract fun formatEntry(entry: LogEntry): FORMAT

    abstract fun shouldLogEntry(entry: LogEntry): Boolean

    abstract fun appendFormattedEntry(formattedEntry: FORMAT)
}