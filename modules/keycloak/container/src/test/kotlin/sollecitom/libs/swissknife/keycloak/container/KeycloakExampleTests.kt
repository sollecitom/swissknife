package sollecitom.libs.swissknife.keycloak.container

import sollecitom.libs.swissknife.test.utils.execution.utils.test
import dasniko.testcontainers.keycloak.KeycloakContainer
import jakarta.ws.rs.ClientErrorException
import kotlinx.coroutines.delay
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.keycloak.admin.client.resource.ClientResource
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.RoleResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.authorization.client.AuthzClient
import org.keycloak.authorization.client.Configuration
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.*
import org.keycloak.representations.idm.authorization.AuthorizationRequest
import org.keycloak.representations.idm.authorization.AuthorizationResponse
import kotlin.time.Duration.Companion.minutes
import org.keycloak.admin.client.Keycloak as KeycloakClient


@Disabled // TODO keycloak is slow to start, only enable this when the exploratory phase will be finished
@TestInstance(PER_CLASS)
class KeycloakExampleTests {

    private val timeout = 5.minutes

    @Test
    fun `standing up keycloak programmatically`() = test(timeout = timeout) {

        val keycloak = Keycloak.newContainer()
        keycloak.start()
        println("Keycloak running on ${keycloak.authServerURI}!")
        val keycloakClient: KeycloakClient = keycloak.keycloakAdminClient

        val realm = keycloakClient.createRealm("a-realm") {
            isUserManagedAccessAllowed = true
        }

        val realms = keycloakClient.realms().findAll()
        println("Realms: ${realms.map { it.realm }}")

        val myApp = realm.createClient("my-app") {
//            isStandardFlowEnabled = true
            isDirectAccessGrantsEnabled = true
        }

        val realmClientNames = realm.clients().map { it.name }
        println("Clients for realm '${realm.name}': $realmClientNames")

        val realmUser = realm.createRole(name = "user", description = "A standard user without admin permissions")
        val realmAdmin = realm.createRole(name = "admin", description = "An admin role with elevated access")
        val myAppUser = myApp.createRole(name = "user", description = "A standard user without admin permissions")
        val myAppAdmin = myApp.createRole(name = "admin", description = "An admin role with elevated access")

        val roleNames = myApp.roles().map { it.name }
        println("Roles: $roleNames")

        val bruce = realm.createUser(
            emailAddress = "bruce@waynecorps.com",
            password = "batman",
            username = "bruce.wayne",
            firstName = "Bruce",
            lastName = "Wayne"
        )

        bruce.addRealmRoles(listOf(realmUser))
        bruce.addClientRoles(myApp, listOf(myAppUser, myAppAdmin))

        val myAppAuthzClient = keycloak.authzClient(realm, myApp)

        val token = with(myAppAuthzClient) { bruce.login() }

        println("Bruce's token type is ${token.tokenType}")
        println("Bruce's ID token is ${token.idToken}")
        println("Bruce's access token is ${token.token}")
        println("Bruce's refresh token is ${token.refreshToken}")
        delay(timeout)
    }
}

fun KeycloakContainer.authzClient(realm: String, clientId: String, clientSecret: String, clientSecretType: String): AuthzClient = AuthzClient.create(Configuration(authServerUrl, realm, clientId, mapOf(clientSecretType to clientSecret), null))

fun KeycloakContainer.authzClient(realm: Realm, client: Client): AuthzClient = authzClient(realm = realm.name, client = client)

fun KeycloakContainer.authzClient(realm: String, client: Client): AuthzClient = authzClient(realm = realm, clientId = client.id, clientSecret = client.secret, clientSecretType = client.secretType)

fun KeycloakClient.createRealm(name: String, isEnabled: Boolean = true, customize: RealmRepresentation.() -> Unit = { }): Realm {

    val realm = RealmRepresentation().also(customize).apply {
        this.realm = name
        this.isEnabled = isEnabled
    }
    try {
        realms().create(realm)
    } catch (error: ClientErrorException) {
        if (error.response.status != HttpStatus.SC_CONFLICT) {
            throw error
        }
    }
    val operations = realms().realm(name)
    val representation = realms().findAll().single { it.realm == name }
    return RealmAdapter(representation, operations)
}

interface Realm {

    val id: String
    val name: String

    fun createClient(name: String, isEnabled: Boolean = true, customize: ClientRepresentation.() -> Unit = {}): Client

    fun clients(): List<Client>

    fun createRole(name: String, description: String = name, customize: RoleRepresentation.() -> Unit = {}): Role

    fun roles(): List<Role>

    // TODO create extensions with RandomGenerator, etc.
    fun createUser(emailAddress: String, password: String, isPasswordTemporary: Boolean = false, username: String = emailAddress, firstName: String? = null, lastName: String? = null, isEmailAddressVerified: Boolean = true, isEnabled: Boolean = true, customize: UserRepresentation.() -> Unit = {}): User
}

internal class RealmAdapter(private val representation: RealmRepresentation, private val operations: RealmResource) : Realm {

    override val id: String get() = representation.id
    override val name: String get() = representation.realm

    override fun createClient(name: String, isEnabled: Boolean, customize: ClientRepresentation.() -> Unit): Client {

        val client = ClientRepresentation().also(customize).apply {
            this.name = name
            this.isEnabled = isEnabled
        }
        val response = operations.clients().create(client)
        if (response.status != HttpStatus.SC_CREATED && response.status != HttpStatus.SC_CONFLICT) {
            error("Unexpected response with status ${response.status} and body '${response.readEntity(String::class.java)}'")
        }
        val representation = operations.clients().findAll().single { it.name == name }
        val operations = operations.clients().get(representation.clientId)
        return ClientAdapter(representation, operations, this)
    }

    override fun clients(): List<Client> = operations.clients().findAll().map { ClientAdapter(it, operations.clients().get(it.clientId), this) }

    override fun createRole(name: String, description: String, customize: RoleRepresentation.() -> Unit): Role {

        val role = RoleRepresentation().also(customize).apply {
            this.name = name
            this.description = description
            this.clientRole = false
            // TODO add attributes and composite
        }
        try {
            operations.roles().create(role)
        } catch (error: ClientErrorException) {
            if (error.response.status != HttpStatus.SC_CONFLICT) {
                throw error
            }
        }
        val operations = this.operations.roles().get(name)
        val representation = this.operations.roles().list().single { it.name == name }
        return RoleAdapter(representation, operations)
    }

    override fun roles(): List<Role> = operations.roles().list().map { RoleAdapter(it, operations.roles().get(it.name)) }

    override fun createUser(emailAddress: String, password: String, isPasswordTemporary: Boolean, username: String, firstName: String?, lastName: String?, isEmailAddressVerified: Boolean, isEnabled: Boolean, customize: UserRepresentation.() -> Unit): User {

        val passwordCredentials = CredentialRepresentation()
        passwordCredentials.isTemporary = isPasswordTemporary
        passwordCredentials.type = CredentialRepresentation.PASSWORD
        passwordCredentials.value = password
        val user = UserRepresentation().also(customize).apply {
            this.username = username
            this.email = emailAddress
            this.isEmailVerified = isEmailAddressVerified
            this.isEnabled = isEnabled
            this.credentials = listOf(passwordCredentials)
            if (firstName != null) {
                this.firstName = firstName
            }
            if (lastName != null) {
                this.lastName = lastName
            }
        }
        val response = operations.users().create(user)
        if (response.status != HttpStatus.SC_CREATED && response.status != HttpStatus.SC_CONFLICT) {
            error("Unexpected response with status ${response.status} and body '${response.readEntity(String::class.java)}'")
        }
        val representation = operations.users().list().single { it.email == emailAddress }
        val operations = operations.users().get(representation.id)
        return UserAdapter(password, representation, operations)
    }
}

interface Client {

    val id: String
    val name: String
    val secretType: String
    val secret: String

    fun createRole(name: String, description: String = name, customize: RoleRepresentation.() -> Unit = {}): Role

    fun roles(): List<Role>
}

internal class ClientAdapter(private val representation: ClientRepresentation, private val operations: ClientResource, private val realm: Realm) : Client {

    override val id: String get() = representation.id
    override val name: String get() = representation.name
    override val secretType: String get() = operations.secret.type
    override val secret: String get() = operations.secret.value

    override fun createRole(name: String, description: String, customize: RoleRepresentation.() -> Unit): Role {

        val role = RoleRepresentation().also(customize).apply {
            this.name = name
            this.description = description
            this.clientRole = true
            // TODO add attributes and composite
        }
        try {
            operations.roles().create(role)
        } catch (error: ClientErrorException) {
            if (error.response.status != HttpStatus.SC_CONFLICT) {
                throw error
            }
        }
        val operations = this.operations.roles().get(name)
        val representation = this.operations.roles().list().single { it.name == name }
        return RoleAdapter(representation, operations)
    }

    override fun roles(): List<Role> = operations.roles().list().map { RoleAdapter(it, operations.roles().get(it.name)) }
}

interface Role {

    val id: String
    val name: String
    val representation: RoleRepresentation
}

internal class RoleAdapter(override val representation: RoleRepresentation, private val operations: RoleResource) : Role {

    override val id: String get() = representation.id
    override val name: String get() = representation.name
}

interface User {

    val id: String
    val username: String
    val emailAddress: String
    val password: String

    fun addRealmRoles(roles: List<Role>)

    fun addClientRoles(client: Client, roles: List<Role>)
}

fun User.login(client: AuthzClient): AccessTokenResponse {

    val response = client.obtainAccessToken(username, password)
    if (response.error != null) error("Got error when trying to obtain access token for user '$username'. Error was description: '${response.errorDescription}', error: '${response.error}', URI: '${response.errorUri}'")
    return response
}

context(client: AuthzClient)
fun User.login(): AccessTokenResponse = login(client = client)

fun User.login2(client: AuthzClient): AuthorizationResponse {

    val request = AuthorizationRequest()
    val response = client.authorization().authorize(request)
    if (response.error != null) error("Got error when trying to obtain access token for user '$username'. Error was description: '${response.errorDescription}', error: '${response.error}', URI: '${response.errorUri}'")
    return response
}

context(client: AuthzClient)
fun User.login2(): AuthorizationResponse = login2(client = client)

internal class UserAdapter(override val password: String, private val representation: UserRepresentation, private val operations: UserResource) : User {

    override val id: String get() = representation.id
    override val username: String get() = representation.username
    override val emailAddress: String get() = representation.email

    override fun addRealmRoles(roles: List<Role>) = operations.roles().realmLevel().add(roles.map(Role::representation))

    override fun addClientRoles(client: Client, roles: List<Role>) = operations.roles().clientLevel(client.id).add(roles.map(Role::representation))
}