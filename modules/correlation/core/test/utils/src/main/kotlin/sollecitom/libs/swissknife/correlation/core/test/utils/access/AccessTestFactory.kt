package sollecitom.libs.swissknife.correlation.core.test.utils.access

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.withoutRoles
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.withContainerStack

context(_: UniqueIdGenerator)
fun Access.Companion.unauthenticated(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create()), isTest: Boolean = false): Access.Unauthenticated = Access.Unauthenticated(origin, authorization, scope, isTest)

context(_: UniqueIdGenerator)
fun Access.Unauthenticated.Companion.create(origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.withoutRoles(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create()), isTest: Boolean = false): Access.Unauthenticated = Access.Unauthenticated(origin, authorization, scope, isTest)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Access.Companion.authenticated(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create())): Access.Authenticated = Access.Authenticated(actor, origin, authorization, scope)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Access.Authenticated.Companion.create(actor: Actor = Actor.direct(), origin: Origin = Origin.create(), authorization: AuthorizationPrincipal = AuthorizationPrincipal.create(), scope: AccessScope = AccessScope.withContainerStack(AccessContainer.create())): Access.Authenticated = Access.Authenticated(actor, origin, authorization, scope)