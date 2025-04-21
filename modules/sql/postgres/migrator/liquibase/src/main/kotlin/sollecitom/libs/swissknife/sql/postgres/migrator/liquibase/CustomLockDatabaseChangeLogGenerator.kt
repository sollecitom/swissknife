package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Databases
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Fields
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.ServicePriority
import sollecitom.libs.swissknife.sql.postgres.migrator.liquibase.Constants.Tables
import liquibase.database.Database
import liquibase.datatype.DataTypeFactory
import liquibase.sql.Sql
import liquibase.sqlgenerator.SqlGeneratorChain
import liquibase.sqlgenerator.SqlGeneratorFactory
import liquibase.sqlgenerator.core.LockDatabaseChangeLogGenerator
import liquibase.statement.core.LockDatabaseChangeLogStatement
import liquibase.statement.core.UpdateStatement
import java.sql.Timestamp

class CustomLockDatabaseChangeLogGenerator : LockDatabaseChangeLogGenerator() {

    @Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")
    override fun generateSql(statement: LockDatabaseChangeLogStatement, database: Database, sqlGeneratorChain: SqlGeneratorChain<LockDatabaseChangeLogStatement>): Array<Sql> {

        val currentClientSession = database.currentClientSession()
        logger.info { "Setting the changelog database table as locked by session $currentClientSession." }
        val updateStatement = database.newChangeLogLockTableUpdateStatement(currentClientSession)
        return SqlGeneratorFactory.getInstance().generateSql(updateStatement, database)
    }

    private fun Database.newChangeLogLockTableUpdateStatement(currentClientSession: DatabaseClientSession): UpdateStatement {

        val updateStatement = newChangeLogLockTableUpdateStatement()
        updateStatement.addNewColumnValue(Fields.LOCKED, true)
        updateStatement.addNewColumnValue(Fields.LOCKED_GRANTED, Timestamp(now.toEpochMilliseconds()))
        updateStatement.addNewColumnValue(Fields.LOCKED_BY, currentClientSession.lockedByValue)
        updateStatement.setWhereClause(escapeColumnName(liquibaseCatalogName, liquibaseSchemaName, databaseChangeLogTableName, Fields.ID) + " = 1 AND " + escapeColumnName(liquibaseCatalogName, liquibaseSchemaName, databaseChangeLogTableName, Fields.LOCKED) + " = " + DataTypeFactory.getInstance().fromDescription("boolean", this).objectToSql(false, this))
        return updateStatement
    }

    private fun Database.newChangeLogLockTableUpdateStatement() = UpdateStatement(liquibaseCatalogName, liquibaseSchemaName, databaseChangeLogLockTableName)

    private fun Database.currentClientSession(): DatabaseClientSession {

        val rs = queryList("SELECT * FROM ${Tables.PG_STAT_ACTIVITY} WHERE ${Fields.PID}=pg_backend_pid()")
        if (rs.isEmpty()) error("Failed to read the current database client active session.")
        if (rs.size > 1) error("Expected exactly 1 row for the current client active session for the database.")
        val row = rs.single()
        val pid = (row[Fields.PID] as Int?).toString()
        val startTime = (row[Fields.BACKEND_START] as Timestamp?).toString()
        return DatabaseClientSession(pid, startTime)
    }

    override fun getPriority() = ServicePriority.MAX

    override fun supports(statement: LockDatabaseChangeLogStatement, database: Database) = database.databaseProductName in Databases.supportedDatabaseNames

    companion object : Loggable()
}