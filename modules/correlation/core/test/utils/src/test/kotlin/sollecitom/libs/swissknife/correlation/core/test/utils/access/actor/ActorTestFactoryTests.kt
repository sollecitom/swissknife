package sollecitom.libs.swissknife.correlation.core.test.utils.access.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.impersonating
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.onBehalfOf
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.federated
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ActorTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Direct {

        @Test
        fun `with given arguments`() {

            val customer = Customer.create()
            val tenant = Tenant.create()
            val account = Actor.Account.user(customer = customer, tenant = tenant)
            val authentication = Authentication.federated(customer = customer, tenant = tenant)

            val actor = Actor.direct(account = account, authentication = authentication)

            assertThat(actor.account).isEqualTo(account)
            assertThat(actor.authentication).isEqualTo(authentication)
        }
    }

    @Nested
    inner class OnBehalf {

        @Test
        fun `with given arguments`() {

            val benefiting = Actor.Account.user()
            val actor = Actor.direct(account = Actor.Account.externalService())

            val onBehalf = actor.onBehalfOf(benefiting = benefiting)

            assertThat(onBehalf.account).isEqualTo(actor.account)
            assertThat(onBehalf.authentication).isEqualTo(actor.authentication)
            assertThat(onBehalf.benefitingAccount).isEqualTo(benefiting)
        }
    }

    @Nested
    inner class Impersonating {

        @Test
        fun `with given arguments`() {

            val impersonated = Actor.Account.user()
            val actor = Actor.direct()

            val impersonating = actor.impersonating(impersonated = impersonated)

            assertThat(impersonating.account).isEqualTo(impersonated)
            assertThat(impersonating.authentication).isEqualTo(actor.authentication)
            assertThat(impersonating.benefitingAccount).isEqualTo(impersonated)
        }
    }
}