package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import liquibase.database.Database
import liquibase.database.core.PostgresDatabase
import liquibase.exception.LockException
import liquibase.lockservice.DatabaseChangeLogLock
import liquibase.lockservice.LockService
import liquibase.statement.core.RawParameterizedSqlStatement
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import java.util.Date
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Serialises migrations with a Postgres session-level advisory lock instead of Liquibase's lock table.
 * Postgres releases the lock when the holding session ends, so a migrator that dies can't leave a stale lock behind.
 */
class PostgresAdvisoryLockService : LockService {

    private lateinit var database: Database
    private var waitTime: Duration = 5.minutes
    private var recheckTime: Duration = 10.seconds
    private var hasLock = false

    override fun supports(database: Database) = database is PostgresDatabase

    override fun getPriority() = Int.MAX_VALUE

    override fun setDatabase(database: Database) {
        this.database = database
    }

    override fun setChangeLogLockWaitTime(changeLogLockWaitTime: Long) {
        waitTime = changeLogLockWaitTime.minutes
    }

    override fun setChangeLogLockRecheckTime(changeLogLockRecheckTime: Long) {
        recheckTime = changeLogLockRecheckTime.seconds
    }

    override fun hasChangeLogLock() = hasLock

    override fun waitForLock() {

        val giveUpAt = Clock.System.now() + waitTime
        while (!acquireLock()) {
            if (Clock.System.now() > giveUpAt) throw LockException("Could not acquire the migrations advisory lock within $waitTime")
            logger.info { "Another session holds the migrations advisory lock. Retrying in $recheckTime" }
            Thread.sleep(recheckTime.inWholeMilliseconds)
        }
    }

    override fun acquireLock(): Boolean {

        if (!hasLock) {
            hasLock = database.jdbc.queryForObject(RawParameterizedSqlStatement("SELECT pg_try_advisory_lock(?)", LOCK_KEY), Boolean::class.javaObjectType)
        }
        return hasLock
    }

    override fun releaseLock() {

        if (hasLock) {
            database.jdbc.queryForObject(RawParameterizedSqlStatement("SELECT pg_advisory_unlock(?)", LOCK_KEY), Boolean::class.javaObjectType)
            hasLock = false
        }
    }

    override fun listLocks(): Array<DatabaseChangeLogLock> {

        val holders = database.jdbc.queryForList(RawParameterizedSqlStatement("SELECT pid FROM pg_locks WHERE locktype = 'advisory' AND granted AND ((classid::bigint << 32) | objid::bigint) = ?", LOCK_KEY))
        return holders.map { holder -> DatabaseChangeLogLock(1, Date(), "pid ${holder.values.single()}") }.toTypedArray()
    }

    override fun forceReleaseLock() = releaseLock()

    override fun reset() {
        hasLock = false
    }

    override fun init() {}

    override fun destroy() {}

    companion object : Loggable() {
        private const val LOCK_KEY = 7_302_190_117_114_101L
    }
}
