package sollecitom.libs.swissknife.correlation.core.test.utils.tenancy

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant

context(UniqueIdGenerator)
fun Tenant.Companion.create(id: Id = newId()): Tenant = Tenant(id)