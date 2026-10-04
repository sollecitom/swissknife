package sollecitom.libs.swissknife.correlation.core.test.utils.toggles

import assertk.assertThat
import assertk.assertions.containsOnly
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withToggle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TogglesTests {

    @Test
    fun `setting a toggle replaces its previous value`() {

        val toggles = Toggles().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH).withToggle(Toggles.InvocationVisibility, InvocationVisibility.DEFAULT)

        assertThat(toggles.values).containsOnly(EnumToggleValue(id = StringId("invocation-visibility"), value = "DEFAULT"))
    }
}
