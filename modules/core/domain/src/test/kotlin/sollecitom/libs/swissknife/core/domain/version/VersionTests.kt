package sollecitom.libs.swissknife.core.domain.version

import assertk.assertThat
import assertk.assertions.isGreaterThan
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class VersionTests {

    @Test
    fun `semantic versions compare their parts numerically`() {

        assertThat<Version>(Version.Semantic(10, 0, 0)).isGreaterThan(Version.Semantic(9, 0, 0))
        assertThat<Version>(Version.Semantic(1, 10, 0)).isGreaterThan(Version.Semantic(1, 9, 0))
        assertThat<Version>(Version.Semantic(1, 0, 10)).isGreaterThan(Version.Semantic(1, 0, 9))
    }
}
