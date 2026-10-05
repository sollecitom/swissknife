package sollecitom.libs.swissknife.http4k.utils

import assertk.assertThat
import assertk.assertions.isNull
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.http4k.client.AsyncHttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD
import kotlin.time.Duration.Companion.milliseconds

@TestInstance(PER_CLASS)
class HttpHandlerExtensionsTests {

    @Test
    @Timeout(value = 5, threadMode = SEPARATE_THREAD)
    fun `invoking an async handler that never responds can be cancelled`() = runTest {

        val neverResponding = object : AsyncHttpHandler {
            override fun invoke(request: Request, fn: (Response) -> Unit) = Unit
        }
        val request = Request(Method.GET, "/test")

        val response = withTimeoutOrNull(100.milliseconds) { neverResponding(request) }

        assertThat(response).isNull()
    }
}