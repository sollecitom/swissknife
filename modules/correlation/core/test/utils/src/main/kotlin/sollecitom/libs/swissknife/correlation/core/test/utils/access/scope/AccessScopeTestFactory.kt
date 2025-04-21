package sollecitom.libs.swissknife.correlation.core.test.utils.access.scope

import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope

fun AccessScope.Companion.withContainerStack(vararg containers: AccessContainer): AccessScope = AccessScope(containers.toList())