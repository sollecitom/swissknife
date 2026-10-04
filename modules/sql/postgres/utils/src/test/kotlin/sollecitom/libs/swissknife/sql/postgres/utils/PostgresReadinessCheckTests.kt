package sollecitom.libs.swissknife.sql.postgres.utils

import assertk.assertThat
import assertk.assertions.isNull
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryMetadata
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Mono
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import sollecitom.libs.swissknife.readiness.domain.checkReadiness
import sollecitom.libs.swissknife.test.utils.execution.utils.test

@TestInstance(PER_CLASS)
class PostgresReadinessCheckTests {

    @Test
    fun `a cancelled check propagates the cancellation instead of reporting a result`() = test {

        val check = PostgresReadinessCheck(DatabaseClient.create(NeverConnectingConnectionFactory))
        var result: ReadinessCheckResult? = null

        val checking = launch(start = CoroutineStart.UNDISPATCHED) { result = check.checkReadiness() }
        checking.cancelAndJoin()

        assertThat(result).isNull()
    }

    private object NeverConnectingConnectionFactory : ConnectionFactory {

        override fun create(): Mono<Connection> = Mono.never()

        override fun getMetadata() = ConnectionFactoryMetadata { "PostgreSQL" }
    }
}
