package sollecitom.libs.swissknife.correlation.core.domain.access.idp

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant

data class IdentityProvider(val name: Name, val customer: Customer, val tenant: Tenant) {

    companion object
}