package sollecitom.libs.swissknife.correlation.core.test.utils.customer

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer

context(UniqueIdGenerator)
fun Customer.Companion.create(id: Id = newId(), isTest: Boolean = false): Customer = Customer(id, isTest)