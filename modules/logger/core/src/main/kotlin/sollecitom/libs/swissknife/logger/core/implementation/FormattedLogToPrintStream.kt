package sollecitom.libs.swissknife.logger.core.implementation

import sollecitom.libs.swissknife.logger.core.FormatLogEntry
import sollecitom.libs.swissknife.logger.core.Log
import sollecitom.libs.swissknife.logger.core.LogEntry
import sollecitom.libs.swissknife.logger.core.utils.log
import java.io.PrintStream

internal class FormattedLogToPrintStream(private val formatLogEntry: FormatLogEntry<String>, private val console: sollecitom.libs.swissknife.logger.core.LoggingLevel.() -> PrintStream) : Log {

    override fun invoke(entry: LogEntry) {

        val text = formatLogEntry(entry)
        val stream = entry.level.console()
        stream.log(text)
    }
}