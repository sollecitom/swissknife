package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.traits.Identifiable
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer

interface Entity : Identifiable {

    sealed interface Reference<out ENTITY : Entity> : Identifiable {

        data class ByValue<ENTITY : Entity>(val value: ENTITY) : Reference<ENTITY> {

            override val id get() = value.id
        }

        data class ByIdAndCustomer(override val id: Id, val customer: Customer) : Reference<Nothing> {

            companion object
        }
    }
}