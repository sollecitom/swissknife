package sollecitom.libs.swissknife.kotlin.extensions.async

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.execution.utils.test

@TestInstance(PER_CLASS)
class DeferredExtensionsTests {

    @Test
    fun `awaiting any cancels the rest when one fails`() = test {

        val scope = CoroutineScope(SupervisorJob())
        val failing = scope.async<Int> { error("boom") }
        val pending = scope.async<Int> { awaitCancellation() }

        val result = runCatching { listOf(failing, pending).awaitAny() }

        assertThat(result).isFailure()
        assertThat(pending.isCancelled).isTrue()
    }
}
