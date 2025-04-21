package sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization

import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles

fun AuthorizationPrincipal.Companion.create(roles: Roles = TestRoles.default): AuthorizationPrincipal = AuthorizationPrincipal(roles)

fun AuthorizationPrincipal.Companion.withoutRoles(): AuthorizationPrincipal = AuthorizationPrincipal(TestRoles.none)