package sollecitom.libs.swissknife.sql.postgres.utils

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import io.r2dbc.pool.ConnectionPool
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.sql.domain.SqlConnectionOptions
import sollecitom.libs.swissknife.sql.reactive.utils.close
import sollecitom.libs.swissknife.sql.reactive.utils.createPooledR2dbcClient
import java.net.URI

@TestInstance(PER_CLASS)
class PooledR2dbcClientTests {

    private val options = SqlConnectionOptions(schemeLessURI = URI("postgresql://localhost:5432/mydb"), user = Name("admin"), password = Password("secret"))

    @Test
    fun `a pooled client connects through a connection pool`() {

        val client = options.createPooledR2dbcClient()

        assertThat(client.connectionFactory).isInstanceOf<ConnectionPool>()
        client.close()
    }

    @Test
    fun `closing a pooled client disposes its connection pool`() {

        val client = options.createPooledR2dbcClient()

        client.close()

        assertThat((client.connectionFactory as ConnectionPool).isDisposed).isTrue()
    }
}
