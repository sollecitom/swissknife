package sollecitom.libs.swissknife.sql.reactive.utils

import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.Disposable
import kotlin.time.Duration
import kotlin.time.toJavaDuration

fun SqlConnectionOptions.createR2dbcClient(connectTimeout: Duration? = null): DatabaseClient = asConnectionFactoryOptions(connectTimeout).build().let(ConnectionFactories::get).let(DatabaseClient::create)

fun SqlConnectionOptions.createPooledR2dbcClient(connectTimeout: Duration? = null): DatabaseClient = asConnectionFactoryOptions(connectTimeout).build().pooled().let(ConnectionFactories::get).let(DatabaseClient::create)

fun DatabaseClient.close() {
    (connectionFactory as? Disposable)?.dispose()
}

fun SqlConnectionOptions.asConnectionFactoryOptions(connectTimeout: Duration? = null): ConnectionFactoryOptions.Builder = ConnectionFactoryOptions.builder().from(ConnectionFactoryOptions.parse(r2dbcURI.toString())).option(ConnectionFactoryOptions.USER, user.value).option(ConnectionFactoryOptions.PASSWORD, password.value).apply { connectTimeout?.toJavaDuration()?.let { option(ConnectionFactoryOptions.CONNECT_TIMEOUT, it) } }

private fun ConnectionFactoryOptions.pooled(): ConnectionFactoryOptions = ConnectionFactoryOptions.builder().from(this).option(PROTOCOL, getRequiredValue(DRIVER).toString()).option(DRIVER, POOLING_DRIVER).build()

private const val POOLING_DRIVER = "pool"
