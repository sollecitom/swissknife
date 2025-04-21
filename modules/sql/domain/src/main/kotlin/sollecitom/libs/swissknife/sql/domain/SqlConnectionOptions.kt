package sollecitom.libs.swissknife.sql.domain

import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.core.domain.text.Name
import java.net.URI

data class SqlConnectionOptions(val schemeLessURI: URI, val user: Name, val password: Password) {

    val r2dbcURI = uriWithScheme(R2DBC)
    val jdbcURI = uriWithScheme(JDBC)

    fun uriWithScheme(scheme: String): URI = "${scheme}:${schemeLessURI}".let(URI::create)

    companion object {
        private const val JDBC = "jdbc"
        private const val R2DBC = "r2dbc"
    }
}