package sollecitom.libs.swissknife.lens.correlation.extensions.toggles

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.lens.Header
import org.http4k.lens.LensFailure
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ToggleValueLensTests {

    private val lens = Header.toggleValue().required(HEADER_NAME)

    @Test
    fun `a toggle value is split on the first equals sign`() {

        val request = Request(GET, "/").header(HEADER_NAME, "some-toggle=a=b")

        val value = lens(request)

        assertThat(value).isEqualTo(EnumToggleValue(StringId("some-toggle"), "a=b"))
    }

    @Test
    fun `a legal value for a standard toggle is accepted`() {

        val request = Request(GET, "/").header(HEADER_NAME, "invocation-visibility=${InvocationVisibility.HIGH.name}")

        val value = lens(request)

        val visibility = setOf(value).let(::Toggles).let(Toggles.InvocationVisibility::invoke)

        assertThat(visibility).isEqualTo(InvocationVisibility.HIGH)
    }

    @Test
    fun `an illegal value for a standard toggle is rejected`() {

        val request = Request(GET, "/").header(HEADER_NAME, "invocation-visibility=not-a-visibility")

        val result = runCatching { lens(request) }

        assertThat(result).failedThrowing<LensFailure>()
    }

    @Test
    fun `a value of the wrong type for a standard toggle is rejected`() {

        val request = Request(GET, "/").header(HEADER_NAME, "invocation-visibility=1")

        val result = runCatching { lens(request) }

        assertThat(result).failedThrowing<LensFailure>()
    }

    private companion object {
        const val HEADER_NAME = "toggle"
    }
}
