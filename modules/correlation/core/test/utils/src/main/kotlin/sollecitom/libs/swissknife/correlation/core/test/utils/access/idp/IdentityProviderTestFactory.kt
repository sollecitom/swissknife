package sollecitom.libs.swissknife.correlation.core.test.utils.access.idp

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create

context(_: UniqueIdGenerator)
fun IdentityProvider.Companion.create(name: Name = "Octa".let(::Name), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): IdentityProvider = IdentityProvider(name, customer, tenant)