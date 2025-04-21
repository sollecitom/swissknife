package sollecitom.libs.swissknife.logger.core.loggable

import sollecitom.libs.swissknife.logger.core.Logger

interface LoggableType {

    val logger: Logger

    fun logger(): Logger

    fun logger(name: String): Logger
}