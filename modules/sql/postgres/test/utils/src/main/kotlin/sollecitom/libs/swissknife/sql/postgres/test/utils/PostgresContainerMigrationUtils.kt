package sollecitom.libs.swissknife.sql.postgres.test.utils

import sollecitom.libs.swissknife.sql.migrator.domain.SqlDatabaseMigrator
import sollecitom.libs.swissknife.sql.migrator.liquibase.applyLiquibaseMigrations
import sollecitom.libs.swissknife.sql.migrator.liquibase.liquibaseDefaultChangelogFilePath
import sollecitom.libs.swissknife.sql.postgres.container.PostgresDockerContainer
import kotlinx.coroutines.runBlocking

suspend fun PostgresDockerContainer.startAndApplyMigrations(changelogFilePath: String = SqlDatabaseMigrator.liquibaseDefaultChangelogFilePath) {

    start()
    SqlDatabaseMigrator.applyLiquibaseMigrations(connectionOptions = connectionOptions(), changelogFilePath = changelogFilePath)
}

fun PostgresDockerContainer.startBlockingAndApplyMigrations(changelogFilePath: String = SqlDatabaseMigrator.liquibaseDefaultChangelogFilePath) = runBlocking { startAndApplyMigrations(changelogFilePath = changelogFilePath) }
