package sollecitom.libs.swissknife.keycloak.container

import dasniko.testcontainers.keycloak.KeycloakContainer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

object Keycloak {

    internal const val NETWORK_ALIAS = "keycloak"
    private const val defaultImageName = "keycloak/keycloak"
    const val defaultAdminUsername = "admin"
    const val defaultAdminPassword = "admin"
    const val defaultImageVersion = "26.0.1"
    const val defaultInitialRamPercentage = 50
    const val defaultMaxRamPercentage = 75

    fun newContainer(version: String = defaultImageVersion, adminUsername: String = defaultAdminUsername, adminPassword: String = defaultAdminPassword, startupAttempts: Int = 10, startupTimeout: Duration = 2.minutes, initialRamPercentage: Int = defaultInitialRamPercentage, maxRamPercentage: Int = defaultMaxRamPercentage, customize: KeycloakContainer.() -> KeycloakContainer = { this }): KeycloakContainer {

        val imageName = "$defaultImageName:$version"
        val keycloak = KeycloakContainer(imageName).customize()
        keycloak.withAdminUsername(adminUsername)
        keycloak.withAdminPassword(adminPassword)
        keycloak.withStartupAttempts(startupAttempts)
        keycloak.withStartupTimeout(startupTimeout.toJavaDuration())
        keycloak.withRamPercentage(initialRamPercentage, maxRamPercentage)
        return keycloak
    }
}