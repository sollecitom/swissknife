package sollecitom.libs.swissknife.logger.core.defaults

import sollecitom.libs.swissknife.logger.core.Log
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.LoggerFactory
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import sollecitom.libs.swissknife.logger.core.implementation.FunctionalLogger
import sollecitom.libs.swissknife.logger.core.implementation.LoggingNameResolver
import java.time.Instant

class DefaultLoggerFactory(defaultLoggingFunction: Log = DefaultLogToConsole) : LoggerFactory {

    private var alreadyConfigured = false
    override var loggingFunction: Log = defaultLoggingFunction
    override var timeNow: () -> Instant = Instant::now
    override var isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean = { true }

    override fun configure(customize: LoggerFactory.Customizer.() -> Unit) {
        check(!alreadyConfigured) { "LoggerFactory can be configured only once." }
        customize(Customizer())
    }

    private inner class Customizer : LoggerFactory.Customizer {

        override var loggingFunction: Log
            get() = this@DefaultLoggerFactory.loggingFunction
            set(value) {
                this@DefaultLoggerFactory.loggingFunction = value
            }
        override var timeNow: () -> Instant
            get() = this@DefaultLoggerFactory.timeNow
            set(value) {
                this@DefaultLoggerFactory.timeNow = value
            }
        override var isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean
            get() = this@DefaultLoggerFactory.isEnabledForLoggerName
            set(value) {
                this@DefaultLoggerFactory.isEnabledForLoggerName = value
            }
    }

    override fun forLoggable(loggable: Any): Logger = logger(LoggingNameResolver.name(loggable::class))

    override fun logger(name: String): Logger = FunctionalLogger(name = name, isEnabledForLoggerName = { levelName -> isEnabledForLoggerName.invoke(this, levelName) }, timeNow = { timeNow() }, log = { entry -> loggingFunction.invoke(entry) })
}