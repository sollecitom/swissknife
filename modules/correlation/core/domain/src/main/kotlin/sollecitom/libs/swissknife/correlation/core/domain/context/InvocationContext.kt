package sollecitom.libs.swissknife.correlation.core.domain.context

import kotlinx.datetime.Instant
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.customerOrNull
import sollecitom.libs.swissknife.correlation.core.domain.access.localeOrNull
import sollecitom.libs.swissknife.correlation.core.domain.access.tenantOrNull
import sollecitom.libs.swissknife.correlation.core.domain.idempotency.IdempotencyContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import java.util.*

data class InvocationContext<out ACCESS : Access>(val access: ACCESS, val trace: Trace, val toggles: Toggles, val specifiedLocale: Locale?, val specifiedTargetCustomer: Customer?, val specifiedTargetTenant: Tenant?) {

    val idempotency: IdempotencyContext = IdempotencyContext(access.idempotencyNamespace, trace.idempotencyKey)

    fun fork(invocation: InvocationTrace): InvocationContext<ACCESS> = copy(trace = trace.fork(invocation))

    companion object
}

val InvocationContext<*>.externalInvocationId get() = trace.external.invocationId
val InvocationContext<*>.externalActionId get() = trace.external.actionId
fun InvocationContext<*>.wasTriggeredByInvocationId(externalInvocationId: Id) = externalInvocationId == this.externalInvocationId

val <ACCESS : Access> InvocationContext<ACCESS>.customerOrNull: Customer? get() = specifiedTargetCustomer ?: authenticatedOrNull()?.access?.customerOrNull
fun <ACCESS : Access> InvocationContext<ACCESS>.customerOrThrow(): Customer = customerOrNull ?: error("Expected target customer not to be null")
val <ACCESS : Access> InvocationContext<ACCESS>.tenantOrNull: Tenant? get() = specifiedTargetTenant ?: authenticatedOrNull()?.access?.tenantOrNull
fun <ACCESS : Access> InvocationContext<ACCESS>.tenantOrThrow(): Tenant = tenantOrNull ?: error("Expected target tenant not to be null")

private val Trace.idempotencyKey: Name get() = external.invocationId.stringValue.let(::Name)

private val Access.idempotencyNamespace: Name? get() = authenticatedOrNull()?.actor?.idempotencyNamespace

private const val internalCustomerSegment = "-internal-"
private const val internalTenantSegment = "-internal-"

private val Actor.idempotencyNamespace: Name get() = IdempotencyContext.combinedNamespace(account.tenant?.id?.stringValue ?: internalTenantSegment, account.customer?.id?.stringValue ?: internalCustomerSegment, account.id.stringValue)

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.authenticatedOrNull(): InvocationContext<Access.Authenticated>? = access.authenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Authenticated> }

fun InvocationContext<Access>.authenticatedOrThrow() = authenticatedOrNull() ?: error("Invocation context is unauthenticated")

fun InvocationContext<Access>.authenticatedOrFailure(): Result<InvocationContext<Access.Authenticated>> = runCatching { authenticatedOrThrow() }

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.unauthenticatedOrNull(): InvocationContext<Access.Unauthenticated>? = access.unauthenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Unauthenticated> }

fun InvocationContext<Access>.unauthenticatedOrThrow() = unauthenticatedOrNull() ?: error("Invocation context is authenticated")

fun InvocationContext<Access>.unauthenticatedOrFailure(): Result<InvocationContext<Access.Unauthenticated>> = runCatching { unauthenticatedOrThrow() }

val InvocationContext<*>.localeOrNull: Locale? get() = specifiedLocale ?: authenticatedOrNull()?.access?.localeOrNull

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun <ACCESS : Access> InvocationContext<ACCESS>.forked(id: Id = ids.newId(), createdAt: Instant = time.now()) = fork(invocation = InvocationTrace(id = id, createdAt = createdAt))