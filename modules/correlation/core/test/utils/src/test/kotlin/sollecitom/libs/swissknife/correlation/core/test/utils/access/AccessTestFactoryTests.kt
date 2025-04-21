package sollecitom.libs.swissknife.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.*
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authenticatedOrFailure
import sollecitom.libs.swissknife.correlation.core.domain.access.authenticatedOrThrow
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.withContainerStack
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import sollecitom.libs.swissknife.test.utils.assertions.succeededWithResult
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Authenticated {

        @Test
        fun `with given arguments`() {

            val origin = Origin.create()
            val actor = Actor.direct()
            val authorization = AuthorizationPrincipal.create()
            val scope = AccessScope.withContainerStack(AccessContainer.create())

            val access = Access.authenticated(actor, origin, authorization, scope)

            assertThat(access.actor).isEqualTo(actor)
            assertThat(access.origin).isEqualTo(origin)
            assertThat(access.authorization).isEqualTo(authorization)
            assertThat(access.scope).isEqualTo(scope)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: Access = Access.authenticated()

            assertThat(access.isAuthenticated).isEqualTo(true)
        }

        @Test
        fun `fluent handling`() {

            val access: Access = Access.authenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNotNull().isEqualTo(access)
        }

        @Test
        fun `fluent handling with error`() {

            val access: Access = Access.authenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).succeededWithResult(access)
        }

        @Test
        fun `fluent handling with result`() {

            val access: Access = Access.authenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isSuccess()
        }
    }

    @Nested
    inner class Unauthenticated {

        @Test
        fun `with default arguments`() {

            val access = Access.unauthenticated()

            assertThat(access.origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
            assertThat(access.authorization.roles).isEmpty()
        }

        @Test
        fun `with given origin`() {

            val origin = Origin.create(ipAddress = IpAddress.create("2001:db8:3333:4444:5555:6666:7777:8888"))

            val access = Access.unauthenticated(origin = origin)

            assertThat(access.origin).isEqualTo(origin)
        }

        @Test
        fun `with given authorization`() {

            val authorization = AuthorizationPrincipal.create()

            val access = Access.unauthenticated(authorization = authorization)

            assertThat(access.authorization).isEqualTo(authorization)
        }

        @Test
        fun `with given access scope`() {

            val container1 = AccessContainer.create()
            val container2 = AccessContainer.create()
            val scope = AccessScope.withContainerStack(container1, container2)

            val access = Access.unauthenticated(scope = scope)

            assertThat(access.scope).isEqualTo(scope)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: Access = Access.unauthenticated()

            assertThat(access.isAuthenticated).isEqualTo(false)
        }

        @Test
        fun `fluent handling`() {

            val access: Access = Access.unauthenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNull()
        }

        @Test
        fun `fluent handling with error`() {

            val access: Access = Access.unauthenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `fluent handling with result`() {

            val access: Access = Access.unauthenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isFailure()
        }

        @Test
        fun `a test unauthenticated access`() {

            val isTest = true

            val access: Access = Access.unauthenticated(isTest = isTest)

            assertThat(access.isTest).isEqualTo(isTest)
        }
    }
}