package sollecitom.libs.swissknife.sql.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URI

@TestInstance(PER_CLASS)
class SqlConnectionOptionsTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class URIGeneration {

        @Test
        fun `jdbc URI is correctly generated`() {

            val options = SqlConnectionOptions(
                schemeLessURI = URI("postgresql://localhost:5432/mydb"),
                user = Name("admin"),
                password = Password("secret")
            )

            assertThat(options.jdbcURI).isEqualTo(URI("jdbc:postgresql://localhost:5432/mydb"))
        }

        @Test
        fun `r2dbc URI is correctly generated`() {

            val options = SqlConnectionOptions(
                schemeLessURI = URI("postgresql://localhost:5432/mydb"),
                user = Name("admin"),
                password = Password("secret")
            )

            assertThat(options.r2dbcURI).isEqualTo(URI("r2dbc:postgresql://localhost:5432/mydb"))
        }

        @Test
        fun `URI with custom scheme`() {

            val options = SqlConnectionOptions(
                schemeLessURI = URI("postgresql://localhost:5432/mydb"),
                user = Name("admin"),
                password = Password("secret")
            )

            val customURI = options.uriWithScheme("custom")

            assertThat(customURI).isEqualTo(URI("custom:postgresql://localhost:5432/mydb"))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Properties {

        @Test
        fun `user and password are stored correctly`() {

            val options = SqlConnectionOptions(
                schemeLessURI = URI("postgresql://localhost:5432/mydb"),
                user = Name("admin"),
                password = Password("secret")
            )

            assertThat(options.user).isEqualTo(Name("admin"))
            assertThat(options.password).isEqualTo(Password("secret"))
        }
    }
}
