package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.traits.Identifiable
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer

/** A domain entity with a unique identity. */
interface Entity : Identifiable {

    /** A reference to an entity, either by value or by ID and customer. */
    sealed interface Reference<out ENTITY : Entity> : Identifiable {

        /** Holds the full entity value. */
        data class ByValue<ENTITY : Entity>(val value: ENTITY) : Reference<ENTITY> {

            override val id get() = value.id
        }

        data class ByIdAndCustomer(override val id: Id, val customer: Customer) : Reference<Nothing> {

            companion object
        }
    }
}