package sollecitom.libs.swissknife.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.isInstanceOf
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
    inner class NoOp {

        @Test
        fun `no-op is a valid result`() {

            val result: EventProcessingResult = EventProcessingResult.NoOp

            assertThat(result).isInstanceOf<EventProcessingResult.NoOp>()
        }
    }
}
