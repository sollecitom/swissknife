package sollecitom.libs.swissknife.web.api.utils.negotiation

import assertk.assertThat
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.ContentNegotiation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
class ContentNegotiationExtensionsTests {

    @Test
    fun `automatic content negotiation requires at least one lens`() {

        val result = runCatching { ContentNegotiation.auto(emptyList<BiDiBodyLens<String>>()) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }
}
