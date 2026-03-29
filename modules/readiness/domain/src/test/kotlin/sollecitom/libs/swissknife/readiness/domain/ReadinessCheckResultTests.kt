package sollecitom.libs.swissknife.readiness.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ReadinessCheckResultTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Passed {

        @Test
        fun `passed result has passed set to true`() {

            val result = ReadinessCheckResult.Passed

            assertThat(result.passed).isTrue()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Failed {

        @Test
        fun `failed result has passed set to false`() {

            val result = ReadinessCheckResult.Failed("connection timeout")

            assertThat(result.passed).isFalse()
        }

        @Test
        fun `failed result stores the reason`() {

            val reason = "database unavailable"
            val result = ReadinessCheckResult.Failed(reason)

            assertThat(result.reason).isEqualTo(reason)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class IfFailed {

        @Test
        fun `ifFailed executes action when result is failed`() {

            val result: ReadinessCheckResult = ReadinessCheckResult.Failed("error")
            var capturedReason = ""

            result.ifFailed { capturedReason = it.reason }

            assertThat(capturedReason).isEqualTo("error")
        }

        @Test
        fun `ifFailed does not execute action when result is passed`() {

            val result: ReadinessCheckResult = ReadinessCheckResult.Passed
            var actionExecuted = false

            result.ifFailed { actionExecuted = true }

            assertThat(actionExecuted).isFalse()
        }
    }
}
