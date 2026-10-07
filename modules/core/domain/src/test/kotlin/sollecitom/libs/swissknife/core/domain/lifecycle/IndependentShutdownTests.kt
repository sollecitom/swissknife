package sollecitom.libs.swissknife.core.domain.lifecycle

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import kotlin.coroutines.cancellation.CancellationException

@TestInstance(PER_CLASS)
class IndependentShutdownTests {

    @Test
    fun `a component failing to stop doesn't prevent the next one from stopping`() = runTest {

        val stopped = mutableListOf<String>()

        stopReportingFailure("first") { error("Boom") }
        stopReportingFailure("second") { stopped += "second" }

        assertThat(stopped).containsExactly("second")
    }

    @Test
    fun `cancellation while stopping is propagated`() = runTest {

        val result = runCatching { stopReportingFailure("component") { throw CancellationException("Cancelled") } }

        assertThat(result).failedThrowing<CancellationException>()
    }

    @Test
    fun `a resource failing to close doesn't prevent the next one from closing`() {

        val closed = mutableListOf<String>()

        closeReportingFailure("first") { error("Boom") }
        closeReportingFailure("second") { closed += "second" }

        assertThat(closed).containsExactly("second")
    }
}
