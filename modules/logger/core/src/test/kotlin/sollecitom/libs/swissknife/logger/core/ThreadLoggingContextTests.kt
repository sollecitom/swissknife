package sollecitom.libs.swissknife.logger.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.slf4j.MDC

@TestInstance(PER_CLASS)
class ThreadLoggingContextTests {

    @AfterEach
    fun clearLoggingContext() = MDC.clear()

    @Test
    fun `the logging context is available while the body runs`() {

        val contextInBody = withThreadLoggingContext(mapOf("invocation-id" to "123")) { MDC.getCopyOfContextMap() }

        assertThat(contextInBody).isEqualTo(mapOf("invocation-id" to "123"))
    }

    @Test
    fun `the previous logging context is restored afterwards`() {

        MDC.put("service", "a-service")

        withThreadLoggingContext(mapOf("invocation-id" to "123")) { }

        assertThat(MDC.getCopyOfContextMap()).isEqualTo(mapOf("service" to "a-service"))
    }

    @Test
    fun `the logging context is cleared afterwards when there was none`() {

        withThreadLoggingContext(mapOf("invocation-id" to "123")) { }

        assertThat(MDC.get("invocation-id")).isNull()
    }
}
