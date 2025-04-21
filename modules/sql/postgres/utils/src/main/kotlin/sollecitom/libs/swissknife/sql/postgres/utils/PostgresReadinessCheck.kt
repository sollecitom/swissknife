package sollecitom.libs.swissknife.sql.postgres.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.readiness.domain.ReadinessAware
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import sollecitom.libs.swissknife.sql.reactive.utils.WithSqlConnectivity
import sollecitom.libs.swissknife.sql.reactive.utils.newTransactionManager
import kotlinx.coroutines.CoroutineScope
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOne
import org.springframework.transaction.ReactiveTransactionManager

class PostgresReadinessCheck(override val sqlClient: DatabaseClient, override val transactionManager: ReactiveTransactionManager = sqlClient.newTransactionManager(), private val adapterName: Name = defaultAdapterName) : ReadinessAware, WithSqlConnectivity.Transactional {

    override val readinessCheckName get() = adapterName
    override val readinessCheck: suspend CoroutineScope.() -> ReadinessCheckResult get() = { tryToConnectToPostgres() }

    private suspend fun CoroutineScope.tryToConnectToPostgres(): ReadinessCheckResult {

        logger.debug { "Attempting to connect to '${adapterName.value}' as part of a readiness check." }
        return try {
            VALIDATION_QUERY.execute().fetch().awaitOne()
            logger.debug { "Successfully connected to '${adapterName.value}' as part of a readiness check." }
            ReadinessCheckResult.Passed
        } catch (error: Exception) {
            logger.debug(error) { "Failed to connect to ${readinessCheckName.value}" }
            ReadinessCheckResult.Failed("Failed to connect to '${adapterName.value}' as part of a readiness check.")
        }
    }

    companion object : Loggable() {
        private val defaultAdapterName = "Postgres".let(::Name)
        private const val VALIDATION_QUERY = "SELECT 1"
    }
}