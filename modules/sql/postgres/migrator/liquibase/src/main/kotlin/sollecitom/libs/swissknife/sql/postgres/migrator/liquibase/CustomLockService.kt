package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Databases
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Fields
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.ServicePriority
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Tables
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Values
import liquibase.database.Database
import liquibase.lockservice.StandardLockService

class CustomLockService : StandardLockService() {

    override fun waitForLock() {
        if (hasDatabaseChangeLogLockTable) {
            val locked = database.isChangeLogLocked()
            if (locked) {
                unlockChangeLogIfLockIsOwnedByAnExpiredDatabaseSessionId()
            } else {
                logger.info { "Database is not locked, no need to try and check for inactive sessions." }
            }
        }
        super.waitForLock()
    }

    private fun unlockChangeLogIfLockIsOwnedByAnExpiredDatabaseSessionId() {

        val lockerSession = database.changeLogLockerSession()
        if (lockerSession == null) {
            logger.info { "Database is locked but there's no information about the client session ID. It isn't safe to unlock the changelog table." }
            return
        }
        if (lockerSession.isActive()) {
            logger.info { "Database is locked by a still active client database session $lockerSession." }
            return
        }
        logger.info { "Database is locked by an inactive client database session $lockerSession. Releasing the lock!" }
        releaseLock()
    }

    private fun DatabaseClientSession.isActive(): Boolean {

        val sql = "SELECT * FROM ${Tables.PG_STAT_ACTIVITY} WHERE ${Fields.PID}=$pid AND ${Fields.BACKEND_START}='$startTime'"
        return database.queryList(sql).isNotEmpty()
    }

    private fun Database.isChangeLogLocked(): Boolean = queryChangeLogLockStatementField(Fields.LOCKED, Boolean::class.javaObjectType) ?: false

    private fun Database.getChangeLogLockedByValue(): String? = queryChangeLogLockStatementField(Fields.LOCKED_BY, String::class.java)?.takeUnless { it.isBlank() }

    private fun Database.changeLogLockerSession(): DatabaseClientSession? = getChangeLogLockedByValue()?.let(::toDatabaseClientSession)

    private fun toDatabaseClientSession(value: String): DatabaseClientSession {

        val tokens = value.split(Values.LOCKED_BY_SEPARATOR)
        if (tokens.size < 2) error("Unable to parse a database client session from raw value '$value'.")
        val (pid, startTime) = tokens
        return DatabaseClientSession(pid = pid, startTime = startTime)
    }

    override fun supports(database: Database) = database.databaseProductName in Databases.supportedDatabaseNames

    override fun getPriority() = ServicePriority.MAX

    companion object : Loggable()
}