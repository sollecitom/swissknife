package sollecitom.libs.swissknife.logger.slf4j.example

import sollecitom.libs.swissknife.logger.core.JvmLoggerFactory
import sollecitom.libs.swissknife.logger.core.LoggingLevel.*
import sollecitom.libs.swissknife.logger.core.appender.PrintStreamAppender
import sollecitom.libs.swissknife.logger.core.defaults.DefaultFormatToString
import sollecitom.libs.swissknife.logger.core.loggingFunction
import sollecitom.libs.swissknife.logger.core.loggingLevelEnabler
import sollecitom.libs.swissknife.logger.core.withCoroutineLoggingContext
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    JvmLoggerFactory.configure {
        loggingFunction = loggingFunction {
            addAppender(PrintStreamAppender(maximumLevel = WARN, stream = System::out, format = DefaultFormatToString))
            addAppender(PrintStreamAppender(minimumLevel = ERROR, stream = System::err, format = DefaultFormatToString))
        }
        isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = TRACE) {
            "sollecitom.libs.swissknife.logger.slf4j" withMinimumLoggingLevel INFO
            "sollecitom.libs.swissknife.logger.slf4j.example" withMinimumLoggingLevel ERROR
            "sollecitom.libs.swissknife.logger.slf4j.abc" withMinimumLoggingLevel INFO
            "sollecitom.libs.swissknife.logger.slf4j.sl" withMinimumLoggingLevel INFO
        }
    }

    val repository: PersonRepository = EmptyPersonRepository()
    val nonexistentPersonId = "1234"

    withCoroutineLoggingContext(mapOf("actorId" to "2345", "traceId" to "3456")) {
        repository.findById(nonexistentPersonId)
        repository.findById("")
    }
}