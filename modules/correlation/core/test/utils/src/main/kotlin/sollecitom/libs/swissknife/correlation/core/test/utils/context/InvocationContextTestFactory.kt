package sollecitom.libs.swissknife.correlation.core.test.utils.context

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.TestRoles
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.unauthenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.toggles.create
import sollecitom.libs.swissknife.correlation.core.test.utils.trace.create
import kotlinx.datetime.Instant
import java.util.*

context(_: UniqueIdGenerator, time: TimeGenerator)
fun InvocationContext.Companion.create(
    timeNow: Instant = time.now(),
    access: (Instant) -> Access = { Access.authenticated() },
    trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
    toggles: (Instant) -> Toggles = { Toggles.create() },
    specifiedLocale: (Instant) -> Locale? = { null },
    specifiedTargetCustomer: (Instant) -> Customer? = { null },
    specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<Access> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedLocale = specifiedLocale(timeNow), specifiedTargetCustomer = specifiedTargetCustomer(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(_: UniqueIdGenerator, time: TimeGenerator)
fun InvocationContext.Companion.authenticated(
    timeNow: Instant = time.now(),
    access: (Instant) -> Access.Authenticated = { Access.authenticated() },
    trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
    toggles: (Instant) -> Toggles = { Toggles.create() },
    specifiedLocale: (Instant) -> Locale? = { null },
    specifiedTargetCustomer: (Instant) -> Customer? = { null },
    specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<Access.Authenticated> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedLocale = specifiedLocale(timeNow), specifiedTargetCustomer = specifiedTargetCustomer(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(_: UniqueIdGenerator, time: TimeGenerator)
fun InvocationContext.Companion.unauthenticated(
    timeNow: Instant = time.now(),
    access: (Instant) -> Access.Unauthenticated = { Access.unauthenticated() },
    trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
    toggles: (Instant) -> Toggles = { Toggles.create() },
    specifiedLocale: (Instant) -> Locale? = { null },
    specifiedTargetCustomer: (Instant) -> Customer? = { null },
    specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<Access.Unauthenticated> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedLocale = specifiedLocale(timeNow), specifiedTargetCustomer = specifiedTargetCustomer(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun InvocationContext.Companion.create(
    timeNow: Instant = time.now(),
    authenticated: Boolean = true,
    roles: Roles = if (authenticated) TestRoles.default else TestRoles.none,
    actor: (Instant) -> Actor = { Actor.direct() },
    origin: Origin = Origin.create(),
    trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
    toggles: (Instant) -> Toggles = { Toggles.create() },
    specifiedLocale: (Instant) -> Locale? = { null },
    specifiedTargetCustomer: (Instant) -> Customer? = { null },
    specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<Access> {

    val authorization = AuthorizationPrincipal.create(roles)
    val access = when {
        authenticated -> Access.authenticated(origin = origin, authorization = authorization, actor = actor(timeNow))
        else -> Access.unauthenticated(origin = origin, authorization = authorization)
    }
    return InvocationContext(access = access, trace = trace(timeNow), toggles = toggles(timeNow), specifiedLocale = specifiedLocale(timeNow), specifiedTargetCustomer = specifiedTargetCustomer(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}