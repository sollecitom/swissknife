package sollecitom.libs.swissknife.pulsar.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.pulsar.client.api.PulsarClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.lang.reflect.Field
import java.lang.reflect.Modifier

@TestInstance(PER_CLASS)
class PulsarClientSettingsTests {

    @Test
    fun `the setting names match the client configuration of the Pulsar version in use`() {

        val configurationFields = PulsarClient.builder().javaClass.getDeclaredField("conf").type.declaredFields
        val settingNames = configurationFields.filter { it.isSetting() }.map { it.name }.toSet()

        assertThat(SettingsDifference(added = settingNames - PulsarClientSettings.names, removed = PulsarClientSettings.names - settingNames)).isEqualTo(SettingsDifference(added = emptySet(), removed = emptySet()))
    }

    private fun Field.isSetting() = !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !isSynthetic && annotations.none { it.annotationClass.simpleName == "JsonIgnore" }

    private data class SettingsDifference(val added: Set<String>, val removed: Set<String>)
}
