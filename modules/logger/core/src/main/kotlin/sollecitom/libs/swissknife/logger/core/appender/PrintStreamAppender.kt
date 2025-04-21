package sollecitom.libs.swissknife.logger.core.appender

import sollecitom.libs.swissknife.logger.core.FormatLogEntry
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logger.core.utils.log
import java.io.PrintStream

class PrintStreamAppender(minimumLevel: LoggingLevel = LoggingLevel.TRACE, maximumLevel: LoggingLevel = LoggingLevel.ERROR, private val stream: () -> PrintStream, format: FormatLogEntry<String>) : AppendLogEntry by FunctionalAppender(format, {
    stream().log(it)
}, { entry -> entry.level in minimumLevel..maximumLevel })