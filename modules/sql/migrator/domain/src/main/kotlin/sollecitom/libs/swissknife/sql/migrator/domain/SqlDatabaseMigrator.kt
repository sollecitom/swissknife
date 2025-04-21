package sollecitom.libs.swissknife.sql.migrator.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.lifecycle.stopBlocking

interface SqlDatabaseMigrator : Stoppable, AutoCloseable {

    suspend fun applyMigrations()

    override fun close() = stopBlocking()

    companion object
}