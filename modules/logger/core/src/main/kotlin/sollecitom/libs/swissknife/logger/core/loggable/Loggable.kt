package sollecitom.libs.swissknife.logger.core.loggable

import sollecitom.libs.swissknife.logger.core.JvmLoggerFactory
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.LoggerFactory

open class Loggable(private val loggerFactory: LoggerFactory = JvmLoggerFactory) : LoggableType {

    override val logger: Logger = logger()

    final override fun logger(): Logger = loggerFactory.forLoggable(this)

    override fun logger(name: String): Logger = loggerFactory.logger(name)
}