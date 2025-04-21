package sollecitom.libs.swissknife.correlation.core.test.utils.access.session

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.correlation.core.domain.access.session.Session
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.access.idp.create
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create

context(ids: UniqueIdGenerator)
fun Session.Companion.simple(id: Id = ids.newId()): SimpleSession = SimpleSession(id)

context(ids: UniqueIdGenerator)
fun Session.Companion.federated(customer: Customer = Customer.create(), tenant: Tenant = Tenant.create(), id: Id = ids.newId(), identityProvider: IdentityProvider = IdentityProvider.create(customer = customer, tenant = tenant)): FederatedSession = FederatedSession(id, identityProvider)

context(ids: UniqueIdGenerator)
fun Session.Companion.federated(identityProvider: IdentityProvider, id: Id = ids.newId()): FederatedSession = FederatedSession(id, identityProvider)