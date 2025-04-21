package sollecitom.libs.swissknife.correlation.core.test.utils.tenancy

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant

context(ids: UniqueIdGenerator)
fun Tenant.Companion.create(id: Id = ids.newId()): Tenant = Tenant(id)