package sollecitom.libs.swissknife.correlation.core.domain.access

import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.customer
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.localeOrNull
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.tenant
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import java.util.*

sealed interface Access {

    val origin: Origin
    val authorization: AuthorizationPrincipal
    val scope: AccessScope
    val isAuthenticated: Boolean
    val isTest: Boolean

    fun authenticatedOrNull(): Authenticated? = takeIf(Access::isAuthenticated)?.let { it as Authenticated }

    fun unauthenticatedOrNull(): Unauthenticated? = takeUnless(Access::isAuthenticated)?.let { it as Unauthenticated }

    data class Unauthenticated(override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope, override val isTest: Boolean) : Access {

        override val isAuthenticated = false

        companion object
    }

    data class Authenticated(val actor: Actor, override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope) : Access {

        override val isAuthenticated = true

        override val isTest get() = actor.isTest

        companion object
    }

    companion object
}

val Access.customerOrNull: Customer? get() = authenticatedOrNull()?.actor?.customer
val Access.tenantOrNull: Tenant? get() = authenticatedOrNull()?.actor?.tenant
val Access.localeOrNull: Locale? get() = authenticatedOrNull()?.actor?.account?.localeOrNull

fun Access.authenticatedOrThrow(): Access.Authenticated = authenticatedOrNull() ?: error("Access is unauthenticated")

fun Access.unauthenticatedOrThrow(): Access.Unauthenticated = unauthenticatedOrNull() ?: error("Access is authenticated")

fun Access.authenticatedOrFailure(): Result<Access.Authenticated> = runCatching { authenticatedOrThrow() }

fun Access.unauthenticatedOrFailure(): Result<Access.Unauthenticated> = runCatching { unauthenticatedOrThrow() }