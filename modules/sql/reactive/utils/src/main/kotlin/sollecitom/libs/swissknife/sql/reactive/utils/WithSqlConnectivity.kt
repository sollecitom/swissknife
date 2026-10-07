package sollecitom.libs.swissknife.sql.reactive.utils

import io.r2dbc.spi.Connection
import io.r2dbc.spi.Readable
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.core.ConnectionAccessor
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import org.springframework.transaction.reactive.transactional

interface WithSqlConnectivity {

    val sqlClient: DatabaseClient

    fun String.execute(bindings: List<Pair<String, Any?>>): DatabaseClient.GenericExecuteSpec = bindings.fold(trimMargin().let(sqlClient::sql)) { spec, binding -> spec + binding }

    fun String.execute(vararg bindings: Pair<String, Any?>) = trimMargin().execute(bindings.toList())

    interface Transactional : WithSqlConnectivity {

        val transactionManager: ReactiveTransactionManager

        suspend fun <T : Any> inTransaction(transactionDefinition: TransactionDefinition = TransactionDefinition.withDefaults(), action: suspend (ReactiveTransaction) -> T?): T? = operator(transactionDefinition).executeAndAwait(action)

        fun <T : Any> Flow<T>.transactional(transactionDefinition: TransactionDefinition = TransactionDefinition.withDefaults()): Flow<T> = transactional(operator(transactionDefinition))

        private fun operator(transactionDefinition: TransactionDefinition) = transactionManager.newTransactionalOperator(transactionDefinition)
    }
}

fun ReactiveTransactionManager.newTransactionalOperator(transactionDefinition: TransactionDefinition = TransactionDefinition.withDefaults()): TransactionalOperator = TransactionalOperator.create(this, transactionDefinition)

fun DatabaseClient.newTransactionManager(): ReactiveTransactionManager = R2dbcTransactionManager(connectionFactory)

data class TypedNull(val type: Class<*>)

inline fun <reified T : Any> nullOf() = TypedNull(T::class.javaObjectType)

operator fun DatabaseClient.GenericExecuteSpec.plus(binding: Pair<String, Any?>): DatabaseClient.GenericExecuteSpec = binding.let { (key, value) ->
    when (value) {
        null -> throw IllegalArgumentException("Parameter '$key' is a bare null: bind nullOf<ColumnType>() instead, so the null carries the column type")
        is TypedNull -> bindNull(key, value.type)
        else -> bind(key, value)
    }
}

fun DatabaseClient.GenericExecuteSpec.bindValueOrNull(key: String, value: Any?, nullType: Class<*>): DatabaseClient.GenericExecuteSpec = when (value) {
    null -> bindNull(key, nullType)
    else -> bind(key, value)
}

inline fun <reified T : Any> DatabaseClient.GenericExecuteSpec.bindValueOrNull(key: String, value: T?): DatabaseClient.GenericExecuteSpec = bindValueOrNull(key, value, T::class.javaObjectType)

fun Readable.debugPrintRowContent(): Readable {
    val row = (this as Row)
    val columnNames = row.metadata.columnMetadatas.map { it.name }.toSet()
    val fields = columnNames.map { it to row[it] }
    println("Row: ${fields.joinToString { "${it.first}=${it.second}" }}")
    return this
}

suspend fun <T : Any> ConnectionAccessor.withConnection(action: suspend Connection.() -> T): T = inConnection { mono { action(it) } }.awaitSingle()

context(connected: WithSqlConnectivity)
suspend fun withConnection(action: suspend Connection.() -> Unit) = connected.sqlClient.withConnection(action)

context(connected: WithSqlConnectivity)
suspend fun <T : Any> returnWithConnection(action: suspend Connection.() -> T) = connected.sqlClient.withConnection(action)