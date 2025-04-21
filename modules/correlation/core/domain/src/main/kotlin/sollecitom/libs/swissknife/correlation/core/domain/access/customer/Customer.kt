package sollecitom.libs.swissknife.correlation.core.domain.access.customer

import sollecitom.libs.swissknife.core.domain.identity.Id

data class Customer(val id: Id, val isTest: Boolean) {

    companion object
}