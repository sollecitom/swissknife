package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import liquibase.Scope
import liquibase.database.Database
import liquibase.executor.Executor
import liquibase.executor.ExecutorService
import liquibase.statement.core.RawParameterizedSqlStatement
import liquibase.statement.core.SelectFromDatabaseChangeLogLockStatement

internal val Database.jdbc: Executor get() = Scope.getCurrentScope().getSingleton(ExecutorService::class.java).getExecutor("jdbc", this)

internal val now: Instant get() = Clock.System.now()

internal fun <T> Database.queryChangeLogLockStatementField(field: String, type: Class<T>) = jdbc.queryForObject(SelectFromDatabaseChangeLogLockStatement(field), type)

internal fun Database.queryList(sql: String) = jdbc.queryForList(RawParameterizedSqlStatement(sql))