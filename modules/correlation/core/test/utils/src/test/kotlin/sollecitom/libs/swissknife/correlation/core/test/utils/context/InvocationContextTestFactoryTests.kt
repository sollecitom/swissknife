package sollecitom.libs.swissknife.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.invoke
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withDefaultValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withToggle
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.user
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.unauthenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.toggles.create
import sollecitom.libs.swissknife.correlation.core.test.utils.trace.create
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import java.util.*

@TestInstance(PER_CLASS)
class InvocationContextTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `customizing the trace`() {

        val actionId = newId.external()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(actionId = actionId))

        val context = InvocationContext.create(trace = { trace })

        assertThat(context.trace).isEqualTo(trace)
    }

    @Test
    fun `customizing the access`() {

        val access = Access.unauthenticated()

        val context = InvocationContext.create(access = { access })

        assertThat(context.access).isEqualTo(access)
    }

    @Test
    fun `customizing the specified locale`() {

        val locale = Locale.CANADA_FRENCH

        val context = InvocationContext.create(specifiedLocale = { locale })

        assertThat(context.specifiedLocale).isEqualTo(locale)
    }

    @Test
    fun `customizing the toggles`() {

        val invocationVisibility = Toggles.InvocationVisibility.withDefaultValue(InvocationVisibility.DEFAULT)
        val overriddenVisibility = InvocationVisibility.HIGH
        val toggles = Toggles().withToggle(Toggles.InvocationVisibility, overriddenVisibility)

        val context = InvocationContext.create(toggles = { toggles })
        val visibility = invocationVisibility.invoke(context)

        assertThat(context.toggles).isEqualTo(toggles)
        assertThat(visibility).isEqualTo(overriddenVisibility)
    }

    @Test
    fun `accessing the toggles when there are no overrides`() {

        val defaultValue = InvocationVisibility.DEFAULT
        val invocationVisibility = Toggles.InvocationVisibility.withDefaultValue(defaultValue)
        val toggles = Toggles()

        val context = InvocationContext.create(toggles = { toggles })
        val visibility = invocationVisibility(context)
        val visibilityOrNull = Toggles.InvocationVisibility(context)

        assertThat(context.toggles).isEqualTo(toggles)
        assertThat(visibility).isEqualTo(defaultValue)
        assertThat(visibilityOrNull).isNull()
    }

    @Test
    fun `deriving idempotency from access and trace`() {

        val invocationId = newId.external()
        val actorId = newId()
        val customerId = newId()
        val tenantId = newId()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(invocationId = invocationId))
        val access = Access.authenticated(actor = Actor.direct(account = Actor.Account.user(id = actorId, customer = Customer.create(id = customerId), tenant = Tenant(id = tenantId))))
        val toggles = Toggles.create()
        val specifiedLocale = null
        val context = InvocationContext(access = access, trace = trace, toggles = toggles, specifiedLocale = specifiedLocale, specifiedTargetCustomer = null, specifiedTargetTenant = null)

        val idempotency = context.idempotency

        assertThat(idempotency.namespace).isNotNull().isEqualTo("${tenantId.stringValue}-${customerId.stringValue}-${actorId.stringValue}".let(::Name))
        assertThat(idempotency.key).isEqualTo(trace.external.invocationId.stringValue.let(::Name))
        assertThat(idempotency.id()).isEqualTo("${idempotency.namespace!!.value}-${idempotency.key.value}".let(::Name))
        assertThat(idempotency.id(separator = "*")).isEqualTo("${idempotency.namespace!!.value}*${idempotency.key.value}".let(::Name))
    }
}