package sollecitom.libs.swissknife.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EventProcessingResultTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Success {

        @Test
        fun `success is a valid result`() {

            val result: EventProcessingResult = EventProcessingResult.Success

            assertThat(result).isInstanceOf<EventProcessingResult.Success>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Failure {

        @Test
        fun `failure wraps an error`() {

            val error = RuntimeException("something went wrong")

            val result = EventProcessingResult.Failure(error)

            assertThat(result.error).isEqualTo(error)
            assertThat(result.message).isEqualTo("something went wrong")
        }

        @Test
        fun `failure with custom message`() {

            val error = RuntimeException("original")

            val result = EventProcessingResult.Failure(error, "custom message")

            assertThat(result.message).isEqualTo("custom message")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NoOp {

        @Test
        fun `no-op is a valid result`() {

            val result: EventProcessingResult = EventProcessingResult.NoOp

            assertThat(result).isInstanceOf<EventProcessingResult.NoOp>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class AsProcessingFailure {

        @Test
        fun `converting a throwable to a processing failure`() {

            val error = IllegalArgumentException("bad input")

            val result = error.asProcessingFailure()

            assertThat(result).isInstanceOf<EventProcessingResult.Failure>()
            assertThat(result.error).isEqualTo(error)
            assertThat(result.message).isNotNull()
        }
    }
}
