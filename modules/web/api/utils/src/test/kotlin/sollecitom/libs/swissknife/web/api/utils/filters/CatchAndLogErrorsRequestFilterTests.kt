package sollecitom.libs.swissknife.web.api.utils.filters

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.standard.output.StandardPrintStream
import sollecitom.libs.swissknife.test.utils.standard.output.withCapturedStandardOutput

@TestInstance(PER_CLASS)
class CatchAndLogErrorsRequestFilterTests {

    @Test
    fun `a failing request is logged by method and path only`() {

        val handler = ServerFilters.catchAndLogErrors.then { error("boom") }
        val request = Request(POST, "/things/42?email=someone@example.com")
            .header("Authorization", "Bearer secret-token")
            .header("Cookie", "session=secret-cookie")
            .body("""{"ssn":"secret-body"}""")

        val (response, lines) = withCapturedStandardOutput(StandardPrintStream.ERR) { handler(request) }
        val output = lines.joinToString(separator = "\n")

        assertThat(response.status).isEqualTo(Status.INTERNAL_SERVER_ERROR)
        assertThat(output).contains("POST /things/42")
        assertThat(output).doesNotContain("secret-token")
        assertThat(output).doesNotContain("secret-cookie")
        assertThat(output).doesNotContain("secret-body")
        assertThat(output).doesNotContain("someone@example.com")
    }
}
