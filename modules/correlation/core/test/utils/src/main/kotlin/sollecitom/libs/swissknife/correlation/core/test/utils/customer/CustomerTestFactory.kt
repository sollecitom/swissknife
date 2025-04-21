package sollecitom.libs.swissknife.correlation.core.test.utils.customer

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer

context(ids: UniqueIdGenerator)
fun Customer.Companion.create(id: Id = ids.newId(), isTest: Boolean = false): Customer = Customer(id, isTest)