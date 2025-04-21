package sollecitom.libs.swissknife.correlation.core.test.utils.toggles

import assertk.assertThat
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.BooleanToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.DecimalToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.test.utils.assertions.containsSameElementsAs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke

@TestInstance(PER_CLASS)
class TogglesTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with given values`() {

        val toggle1 = BooleanToggleValue(id = newId(), value = true)
        val toggle2 = DecimalToggleValue(id = newId(), value = 12.5)
        val toggle3 = EnumToggleValue(id = newId(), value = "HIGH")
        val values = setOf(toggle1, toggle2, toggle3)

        val toggles = Toggles.create(values = values)

        assertThat(toggles.values).containsSameElementsAs(values)
    }
}