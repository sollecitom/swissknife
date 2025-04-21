package sollecitom.libs.swissknife.correlation.core.test.utils.access.actor

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.credentialsBased
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import java.util.*

context(ids: UniqueIdGenerator)
fun Actor.Account.Companion.user(id: Id = ids.newId(), locale: Locale = Locale.UK, customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()) = Actor.UserAccount(id, locale, customer, tenant)

context(ids: UniqueIdGenerator)
fun Actor.UserAccount.Companion.create(id: Id = ids.newId(), locale: Locale = Locale.UK, customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.UserAccount = Actor.UserAccount(id, locale, customer, tenant)

context(ids: UniqueIdGenerator)
fun Actor.Account.Companion.externalService(id: Id = ids.newId(), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount.External = Actor.ServiceAccount.External(id, customer, tenant)

context(ids: UniqueIdGenerator)
fun Actor.ServiceAccount.External.Companion.create(id: Id = ids.newId(), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount.External = Actor.ServiceAccount.External(id, customer, tenant)

context(ids: UniqueIdGenerator)
fun Actor.Account.Companion.internalService(id: Id = ids.newId()): Actor.ServiceAccount.Internal = Actor.ServiceAccount.Internal(id)

context(ids: UniqueIdGenerator)
fun Actor.ServiceAccount.Internal.Companion.create(id: Id = ids.newId()): Actor.ServiceAccount.Internal = Actor.ServiceAccount.Internal(id)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Actor.Companion.direct(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun DirectActor.Companion.create(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Actor.Companion.impersonating(impersonating: Actor.Account = Actor.UserAccount.create(), impersonator: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): ImpersonatingActor = ImpersonatingActor(impersonator = impersonating, impersonated = impersonator, authentication = authentication)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun ImpersonatingActor.Companion.create(impersonating: Actor.Account = Actor.UserAccount.create(), impersonator: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): ImpersonatingActor = ImpersonatingActor(impersonator = impersonating, impersonated = impersonator, authentication = authentication)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Actor.Companion.onBehalf(account: Actor.Account = Actor.UserAccount.create(), benefitingAccount: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): ActorOnBehalf = ActorOnBehalf(account = account, benefitingAccount = benefitingAccount, authentication = authentication)

context(_: UniqueIdGenerator, _: TimeGenerator)
fun ActorOnBehalf.Companion.create(account: Actor.Account = Actor.UserAccount.create(), benefitingAccount: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): ActorOnBehalf = ActorOnBehalf(account = account, benefitingAccount = benefitingAccount, authentication = authentication)