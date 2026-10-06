package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.sql.postgres.container.newPostgresContainer
import java.sql.DriverManager

@TestInstance(PER_CLASS)
class PostgresAdvisoryLockServiceTests {

    private val postgres = newPostgresContainer().apply { start() }

    @Test
    fun `a second migrator can't take the lock until the first releases it`() {

        val first = newLockService()
        val second = newLockService()

        val firstAcquired = first.acquireLock()
        val secondAcquiredWhileHeld = second.acquireLock()
        first.releaseLock()
        val secondAcquiredAfterRelease = second.acquireLock()

        assertThat(firstAcquired).isTrue()
        assertThat(secondAcquiredWhileHeld).isFalse()
        assertThat(secondAcquiredAfterRelease).isTrue()
        second.releaseLock()
    }

    @Test
    fun `the lock is freed when the migrator holding it dies`() {

        val dying = newDatabase()
        val holder = newLockService(dying)
        val next = newLockService()
        holder.acquireLock()

        dying.close()
        val acquiredAfterHolderDied = next.acquireLock()

        assertThat(acquiredAfterHolderDied).isTrue()
        next.releaseLock()
    }

    @AfterAll
    fun stopPostgres() = postgres.stop()

    private fun newLockService(database: Database = newDatabase()) = PostgresAdvisoryLockService().apply { setDatabase(database) }

    private fun newDatabase(): Database {

        val options = postgres.connectionOptions()
        val connection = DriverManager.getConnection(options.jdbcURI.toString(), options.user.value, options.password.value)
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
    }
}
