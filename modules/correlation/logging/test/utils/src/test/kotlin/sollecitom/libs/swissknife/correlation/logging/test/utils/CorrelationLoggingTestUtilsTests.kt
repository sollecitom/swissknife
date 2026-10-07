package sollecitom.libs.swissknife.correlation.logging.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.core.test.utils.testProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.slf4j.MDC

@TestInstance(PER_CLASS)
class CorrelationLoggingTestUtilsTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `the test body runs with the invocation context in the logging context`() {

        var loggingContext: Map<String, String>? = null

        testWithInvocationLoggingContext(convert = { "converted" }) { _ ->
            loggingContext = MDC.getCopyOfContextMap()
        }

        assertThat(loggingContext).isEqualTo(mapOf("invocation" to "converted"))
    }
}
