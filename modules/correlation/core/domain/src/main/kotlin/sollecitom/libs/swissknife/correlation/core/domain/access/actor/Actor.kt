package sollecitom.libs.swissknife.correlation.core.domain.access.actor

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import java.util.*

sealed interface Actor {

    val account: Account
    val benefitingAccount: Account
    val authentication: Authentication
    val isTest: Boolean get() = account.isTest

    sealed interface Account {
        val id: Id
        val customer: Customer?
        val tenant: Tenant?

        val isTest: Boolean get() = customer?.isTest ?: false

        companion object
    }

    data class UserAccount(override val id: Id, val locale: Locale?, override val customer: Customer, override val tenant: Tenant) : Account {

        companion object
    }

    sealed interface ServiceAccount : Account {

        data class External(override val id: Id, override val customer: Customer, override val tenant: Tenant) : ServiceAccount {

            companion object
        }

        data class Internal(override val id: Id) : ServiceAccount {

            override val tenant = null
            override val customer = null

            companion object
        }

        companion object
    }

    companion object
}

val Actor.Account.localeOrNull: Locale? get() = if (this is Actor.UserAccount) locale else null

val Actor.tenant: Tenant? get() = account.tenant
val Actor.customer: Customer? get() = account.customer