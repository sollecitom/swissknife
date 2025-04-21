package sollecitom.libs.swissknife.logger.slf4j.adapter

import sollecitom.libs.swissknife.logger.core.JvmLoggerFactory
import org.slf4j.ILoggerFactory
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.Logger as Slf4jLogger

class Slf4jLoggerFactory : ILoggerFactory {

    private val loggers = ConcurrentHashMap<String, Slf4jLogger>()

    override fun getLogger(name: String): Slf4jLogger = loggers.computeIfAbsent(name) { JvmLoggerFactory.logger(name).let(::Slf4jLoggerAdapter) }
}