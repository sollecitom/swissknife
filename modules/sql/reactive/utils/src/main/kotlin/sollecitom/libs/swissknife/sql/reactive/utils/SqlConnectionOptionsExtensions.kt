package sollecitom.libs.swissknife.sql.reactive.utils

import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.r2dbc.core.DatabaseClient
import kotlin.time.Duration
import kotlin.time.toJavaDuration

fun SqlConnectionOptions.createR2dbcClient(connectTimeout: Duration? = null): DatabaseClient = DatabaseClient.create(ConnectionFactories.get(asConnectionFactoryOptions(connectTimeout).build()))

fun SqlConnectionOptions.asConnectionFactoryOptions(connectTimeout: Duration? = null): ConnectionFactoryOptions.Builder = ConnectionFactoryOptions.builder().from(ConnectionFactoryOptions.parse(r2dbcURI.toString())).option(ConnectionFactoryOptions.USER, user.value).option(ConnectionFactoryOptions.PASSWORD, password.value).apply { connectTimeout?.toJavaDuration()?.let { option(ConnectionFactoryOptions.CONNECT_TIMEOUT, it) } }