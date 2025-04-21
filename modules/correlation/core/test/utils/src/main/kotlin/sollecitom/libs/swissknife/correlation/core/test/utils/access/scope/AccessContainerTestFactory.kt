package sollecitom.libs.swissknife.correlation.core.test.utils.access.scope

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer

context(UniqueIdGenerator)
fun AccessContainer.Companion.create(id: Id = newId()): AccessContainer = AccessContainer(id)