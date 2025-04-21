package sollecitom.libs.swissknife.sql.postgres.container

import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.postgres.container.PostgresDockerContainer.Companion.defaultImageVersion
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT
import org.testcontainers.utility.DockerImageName
import java.net.URI

private const val POSTGRES_NETWORK_ALIAS = "postgres"
private const val ROOT_USER = "root"

fun newPostgresContainer(databaseName: String = "db", username: String = "user", password: String = "password", network: Network? = null, postgresVersion: String = defaultImageVersion): PostgresDockerContainer {

    return PostgresDockerContainer(imageVersion = postgresVersion).withDatabaseName(databaseName).withUsername(username).withPassword(password).also { container -> network?.let { container.withNetwork(network).withNetworkAliases(POSTGRES_NETWORK_ALIAS) } }
}

class PostgresDockerContainer(imageName: DockerImageName = DockerImageName.parse(defaultImageName), imageVersion: String = defaultImageVersion) : PostgreSQLContainer<PostgresDockerContainer>(imageName.withTag(imageVersion)) {

    fun connectionOptions(username: Name = this.username.let(::Name), password: Password = this.password.let(::Password)): SqlConnectionOptions {

        val schemeLessURI = "postgresql://$host:${getMappedPort(POSTGRESQL_PORT)}/${databaseName}".let(URI::create)
        return SqlConnectionOptions(schemeLessURI, username, password)
    }

    companion object {
        const val defaultImageName = "postgres"
        const val defaultImageVersion = "14.2"
    }
}

fun PostgresDockerContainer.connectionOptions(rootUser: Boolean = false): SqlConnectionOptions {

    return connectionOptions(username.let(::Name).takeUnless { rootUser } ?: ROOT_USER.let(::Name), password.let(::Password))
}

fun PostgresDockerContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(POSTGRES_NETWORK_ALIAS)) = withNetwork(network).withNetworkAliases(*aliases)

val PostgresDockerContainer.networkAlias: String get() = POSTGRES_NETWORK_ALIAS

val PostgresDockerContainer.Companion.PORT: Int get() = POSTGRESQL_PORT