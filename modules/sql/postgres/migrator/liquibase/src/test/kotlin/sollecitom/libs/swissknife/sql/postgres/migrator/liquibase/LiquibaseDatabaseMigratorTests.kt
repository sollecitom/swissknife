package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.migrator.domain.SqlDatabaseMigrator
import sollecitom.libs.swissknife.sql.migrator.liquibase.liquibase
import sollecitom.libs.swissknife.sql.postgres.container.newPostgresContainer
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URI
import java.sql.DriverManager
import java.sql.SQLException

@TestInstance(PER_CLASS)
class LiquibaseDatabaseMigratorTests {

    private val postgres = newPostgresContainer().apply { start() }

    @Test
    fun `a migrator can apply migrations more than once`() = test {

        val migrator = SqlDatabaseMigrator.liquibase(postgres.connectionOptions())

        migrator.applyMigrations()
        val secondRun = runCatching { migrator.applyMigrations() }
        migrator.stop()

        assertThat(secondRun).isSuccess()
    }

    @Test
    fun `stopping a migrator that never ran does not connect`() = test {

        val unreachable = SqlConnectionOptions(URI.create("postgresql://localhost:1/nowhere"), Name("user"), Password("password"))
        val migrator = SqlDatabaseMigrator.liquibase(unreachable)

        val result = runCatching { migrator.stop() }

        assertThat(result).isSuccess()
    }

    @Test
    fun `a created user can read and write data but cannot change the schema`() = test {

        SqlDatabaseMigrator.liquibase(postgres.connectionOptions()).use { it.applyMigrations() }
        val options = postgres.createUser(Name("restricted_user"), Password("restricted-password"))

        DriverManager.getConnection(options.jdbcURI.toString(), options.user.value, options.password.value).use { connection ->
            connection.createStatement().use { it.executeUpdate("INSERT INTO greetings (text) VALUES ('hello')") }
            val count = connection.createStatement().use { statement -> statement.executeQuery("SELECT COUNT(*) FROM greetings WHERE text = 'hello'").use { it.next(); it.getLong(1) } }
            val ddl = runCatching { connection.createStatement().use { it.execute("CREATE TABLE forbidden (id INT)") } }

            assertThat(count).isEqualTo(1L)
            assertThat(ddl).failedThrowing<SQLException>()
        }
    }

    @AfterAll
    fun stopPostgres() = postgres.stop()
}
