package sollecitom.libs.swissknife.keycloak.container

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class KeycloakContainerConfigurationTests {

    @Test
    fun `customizations override the default settings`() {

        val keycloak = Keycloak.newContainer { withAdminUsername("custom-admin").withAdminPassword("custom-password") }

        assertThat(keycloak.adminUsername).isEqualTo("custom-admin")
        assertThat(keycloak.adminPassword).isEqualTo("custom-password")
    }
}
