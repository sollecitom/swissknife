package sollecitom.libs.swissknife.sql.postgres.container

import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.postgres.container.PostgresDockerContainer.Companion.defaultImageVersion
import org.testcontainers.containers.Network
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.postgresql.PostgreSQLContainer.POSTGRESQL_PORT
import org.testcontainers.utility.DockerImageName
import java.net.URI

private const val POSTGRES_NETWORK_ALIAS = "postgres"
private const val ROOT_USER = "root"

fun newPostgresContainer(databaseName: String = "db", username: String = "user", password: String = "password", network: Network? = null, postgresVersion: String = defaultImageVersion): PostgresDockerContainer {

    return PostgresDockerContainer(imageVersion = postgresVersion).withDatabaseName(databaseName).withUsername(username).withPassword(password).also { container -> network?.let { container.withNetwork(network).withNetworkAliases(POSTGRES_NETWORK_ALIAS) } }
}

/**
 * Wraps testcontainers' [PostgreSQLContainer] via composition rather than inheritance. testcontainers 2.x
 * dropped the `<SELF>` type parameter, so subclassing would lose the fluent self-typed return values; composing
 * lets us keep our own fluent API while staying on the supported (non-deprecated) class.
 */
class PostgresDockerContainer(imageName: DockerImageName = DockerImageName.parse(defaultImageName), imageVersion: String = defaultImageVersion) : AutoCloseable {

    private val container: PostgreSQLContainer = PostgreSQLContainer(imageName.withTag(imageVersion))

    fun withDatabaseName(name: String): PostgresDockerContainer = also { container.withDatabaseName(name) }

    fun withUsername(value: String): PostgresDockerContainer = also { container.withUsername(value) }

    fun withPassword(value: String): PostgresDockerContainer = also { container.withPassword(value) }

    fun withNetwork(network: Network): PostgresDockerContainer = also { container.withNetwork(network) }

    fun withNetworkAliases(vararg aliases: String): PostgresDockerContainer = also { container.withNetworkAliases(*aliases) }

    fun start() = container.start()

    fun stop() = container.stop()

    override fun close() = stop()

    val host: String get() = container.host

    val databaseName: String get() = container.databaseName

    val username: String get() = container.username

    val password: String get() = container.password

    fun getMappedPort(port: Int): Int = container.getMappedPort(port)

    fun connectionOptions(username: Name = this.username.let(::Name), password: Password = this.password.let(::Password)): SqlConnectionOptions {

        val schemeLessURI = "postgresql://$host:${getMappedPort(POSTGRESQL_PORT)}/${databaseName}".let(URI::create)
        return SqlConnectionOptions(schemeLessURI, username, password)
    }

    companion object {
        const val defaultImageName = "postgres"
        const val defaultImageVersion = "15"
    }
}

fun PostgresDockerContainer.connectionOptions(rootUser: Boolean = false): SqlConnectionOptions {

    return connectionOptions(username.let(::Name).takeUnless { rootUser } ?: ROOT_USER.let(::Name), password.let(::Password))
}

fun PostgresDockerContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(POSTGRES_NETWORK_ALIAS)) = withNetwork(network).withNetworkAliases(*aliases)

val PostgresDockerContainer.networkAlias: String get() = POSTGRES_NETWORK_ALIAS

val PostgresDockerContainer.Companion.PORT: Int get() = POSTGRESQL_PORT
