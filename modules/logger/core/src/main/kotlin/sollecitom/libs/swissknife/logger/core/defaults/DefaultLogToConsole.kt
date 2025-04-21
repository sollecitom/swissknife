package sollecitom.libs.swissknife.logger.core.defaults

import sollecitom.libs.swissknife.logger.core.Log
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logger.core.implementation.FormattedLogToPrintStream

object DefaultLogToConsole : Log by FormattedLogToPrintStream(DefaultFormatToString, { if (this == LoggingLevel.ERROR) System.err else System.out })