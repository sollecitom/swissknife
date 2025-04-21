package sollecitom.libs.swissknife.sql.migrator.liquibase

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.migrator.domain.SqlDatabaseMigrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Scope
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.visitor.ChangeExecListener
import liquibase.command.CommandScope
import liquibase.command.core.UpdateCommandStep
import liquibase.command.core.helpers.ChangeExecListenerCommandStep
import liquibase.command.core.helpers.DatabaseChangelogCommandStep
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.parser.core.yaml.YamlChangeLogParser
import liquibase.resource.ClassLoaderResourceAccessor
import java.io.OutputStream
import java.sql.Connection
import java.sql.DriverManager

internal class LiquibaseDatabaseMigrator(private val connectionOptions: SqlConnectionOptions, private val changelogFilePath: String = defaultChangelogFilePath) : SqlDatabaseMigrator {

    private val jdbcConnection by lazy { DriverManager.getConnection(connectionOptions.jdbcURI.toString(), connectionOptions.user.value, connectionOptions.password.value) }

    init {
        initializeYamlParser()
    }

    override suspend fun applyMigrations() {

        logger.info { "Started running migrations from $changelogFilePath against ${connectionOptions.jdbcURI}" }
        withCorrectDatabaseInScope(jdbcConnection) { database ->
            val updateCommand = updateCommand(database, changelogFilePath)
            updateCommand.execute()
        }
        logger.info { "Finished running migrations from $changelogFilePath against ${connectionOptions.jdbcURI}" }
    }

    override suspend fun stop() = jdbcConnection.close()

    private fun updateCommand(database: Database, changelogFilePath: String): CommandScope {

        val command = CommandScope(*UpdateCommandStep.COMMAND_NAME)
        command.apply {
            addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database)
            addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changelogFilePath)
            addArgumentValue(UpdateCommandStep.CONTEXTS_ARG, Contexts().toString())
            addArgumentValue(UpdateCommandStep.LABEL_FILTER_ARG, LabelExpression().originalString)
            addArgumentValue(ChangeExecListenerCommandStep.CHANGE_EXEC_LISTENER_ARG, null as ChangeExecListener?)
            addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS, ChangeLogParameters(database))
            setOutput(OutputStream.nullOutputStream()) // Suppress output to stdout (logging will still occur)
        }
        return command
    }

    private fun initializeYamlParser() {

        YamlChangeLogParser() // initializes the Yaml parser
    }

    private suspend fun withCorrectDatabaseInScope(jdbcConnection: Connection, action: (Database) -> Unit) = withContext(Dispatchers.IO) {

        val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(jdbcConnection))
        database.use {
            val scopeObjects = mapOf(
                Scope.Attr.database.name to database,
                Scope.Attr.resourceAccessor.name to ClassLoaderResourceAccessor()
            )
            Scope.child(scopeObjects) {
                action(database)
            }
        }
    }

    companion object : Loggable() {

        internal const val defaultChangelogFilePath = "database/migrations/changelog.yaml"
    }
}