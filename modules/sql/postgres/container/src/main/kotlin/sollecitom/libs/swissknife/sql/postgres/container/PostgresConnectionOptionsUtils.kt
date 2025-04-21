package sollecitom.libs.swissknife.sql.postgres.container

import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.reactive.utils.asConnectionFactoryOptions
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider
import io.r2dbc.postgresql.codec.EnumCodec
import org.springframework.r2dbc.core.DatabaseClient
import kotlin.time.Duration

internal fun SqlConnectionOptions.createPostgresR2dbcClient(connectTimeout: Duration? = null, vararg enumsToRegister: Pair<String, Class<out Enum<*>>>): DatabaseClient {

    require(enumsToRegister.isNotEmpty()) { "Postgres specific client is designed for enum support (of which none have been passed in), use createSqlClient() instead" }
    val registrar = enumsToRegister.fold(EnumCodec.builder()) { builder, (name, enum) -> builder.withEnum(name, enum) }.build()
    return PostgresqlConnectionFactoryProvider.builder(asConnectionFactoryOptions(connectTimeout).build()).codecRegistrar(registrar).build().let(::PostgresqlConnectionFactory).let(DatabaseClient::create)
}