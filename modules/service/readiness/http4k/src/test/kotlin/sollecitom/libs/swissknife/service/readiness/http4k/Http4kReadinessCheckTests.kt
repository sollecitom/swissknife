package sollecitom.libs.swissknife.service.readiness.http4k

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.CoroutineScope
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.readiness.domain.ReadinessAware
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.milliseconds

@TestInstance(PER_CLASS)
class Http4kReadinessCheckTests {

    @Test
    fun `a check that does not answer in time reports not ready`() {

        val check = readinessAware { awaitCancellation() }.http4kReadinessCheckWithModuleName(Name("module"), timeout = 100.milliseconds)

        val result = check()

        assertThat(result.pass).isFalse()
    }

    @Test
    fun `a passing check reports ready`() {

        val check = readinessAware { ReadinessCheckResult.Passed }.http4kReadinessCheck

        val result = check()

        assertThat(result.pass).isTrue()
    }

    private fun readinessAware(check: suspend CoroutineScope.() -> ReadinessCheckResult) = object : ReadinessAware {
        override val readinessCheckName = Name("check")
        override val readinessCheck = check
    }
}
