package sollecitom.libs.swissknife.keycloak.container

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.keycloak.admin.client.KeycloakBuilder
import org.testcontainers.containers.Network
import java.net.URI
import org.keycloak.admin.client.Keycloak as KeyCloakClient

fun KeycloakContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(Keycloak.NETWORK_ALIAS)): KeycloakContainer = withNetwork(network).withNetworkAliases(*aliases)

val KeycloakContainer.networkAlias: String get() = Keycloak.NETWORK_ALIAS

val KeycloakContainer.authServerURI: URI get() = URI.create(authServerUrl)

fun KeycloakContainer.adminClient(realm: String = KeycloakContainer.MASTER_REALM, username: String = adminUsername, password: String = adminPassword, clientId: String = KeycloakContainer.ADMIN_CLI_CLIENT, customize: KeycloakBuilder.() -> KeycloakBuilder = { this }): KeyCloakClient = KeycloakBuilder.builder().customize()
    .serverUrl(authServerUrl)
    .realm(realm)
    .clientId(clientId)
    .username(username)
    .password(adminPassword)
    .build()