package sollecitom.libs.swissknife.sql.migrator.liquibase

import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.migrator.domain.SqlDatabaseMigrator

fun SqlDatabaseMigrator.Companion.liquibase(connectionOptions: SqlConnectionOptions, changelogFilePath: String = liquibaseDefaultChangelogFilePath): SqlDatabaseMigrator {

    val migrator = LiquibaseDatabaseMigrator(connectionOptions, changelogFilePath)
    return migrator
}

suspend fun SqlDatabaseMigrator.Companion.applyLiquibaseMigrations(connectionOptions: SqlConnectionOptions, changelogFilePath: String = SqlDatabaseMigrator.liquibaseDefaultChangelogFilePath) {

    SqlDatabaseMigrator.liquibase(connectionOptions, changelogFilePath).use { it.applyMigrations() }
}

val SqlDatabaseMigrator.Companion.liquibaseDefaultChangelogFilePath: String get() = LiquibaseDatabaseMigrator.defaultChangelogFilePath