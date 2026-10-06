package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import liquibase.Scope
import liquibase.database.Database
import liquibase.executor.Executor
import liquibase.executor.ExecutorService

internal val Database.jdbc: Executor get() = Scope.getCurrentScope().getSingleton(ExecutorService::class.java).getExecutor("jdbc", this)
