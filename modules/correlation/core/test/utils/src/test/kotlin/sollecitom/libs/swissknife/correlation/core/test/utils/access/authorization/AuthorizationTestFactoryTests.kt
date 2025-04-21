package sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AuthorizationTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `the default roles are set when no arguments are explicitly passed`() {

        val authorization = AuthorizationPrincipal.create()

        assertThat(authorization.roles).isEqualTo(TestRoles.default)
    }

    @Test
    fun `configuring the roles explicitly`() {

        val anotherRole = "some-other-role".let(::Name).let(::Role)
        val roles = Roles(setOf(TestRoles.readOnlyUser, anotherRole))

        val authorization = AuthorizationPrincipal.create(roles)

        assertThat(authorization.roles).isEqualTo(roles)
    }
}