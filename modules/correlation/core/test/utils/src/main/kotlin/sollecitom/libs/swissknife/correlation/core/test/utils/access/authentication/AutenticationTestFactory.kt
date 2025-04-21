package sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.FederatedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.StatelessAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.correlation.core.domain.access.session.Session
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.access.session.federated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.session.simple
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import kotlinx.datetime.Instant
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import kotlin.time.Duration.Companion.minutes

context(ids: UniqueIdGenerator)
fun Authentication.Token.Companion.create(validTo: Instant, id: Id = ids.newId(), validFrom: Instant? = null): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(ids: UniqueIdGenerator)
fun Authentication.Token.Companion.create(id: Id = ids.newId(), validTo: Instant? = null, validFrom: Instant): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(ids: UniqueIdGenerator)
fun Authentication.Token.Companion.create(validFrom: Instant, validTo: Instant, id: Id = ids.newId()): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun Authentication.Token.Companion.create(timeNow: Instant = time.now(), id: Id = ids.newId(), validFrom: Instant? = timeNow - 5.minutes, validTo: Instant? = timeNow + 25.minutes): Authentication.Token = Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)

context(_: UniqueIdGenerator, time: TimeGenerator)
fun Authentication.Companion.credentialsBased(timeNow: Instant = time.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: SimpleSession = Session.simple()): CredentialsBasedAuthentication = CredentialsBasedAuthentication(token = token, session = session)

context(_: UniqueIdGenerator, time: TimeGenerator)
fun Authentication.Companion.federated(timeNow: Instant = time.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: FederatedSession = Session.federated()): FederatedAuthentication = FederatedAuthentication(token = token, session = session)

context(_: UniqueIdGenerator, time: TimeGenerator)
fun Authentication.Companion.federated(timeNow: Instant = time.now(), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow), session: FederatedSession = Session.federated(customer = customer, tenant = tenant)): FederatedAuthentication = FederatedAuthentication(token = token, session = session)

context(_: UniqueIdGenerator, time: TimeGenerator)
fun Authentication.Companion.stateless(timeNow: Instant = time.now(), token: Authentication.Token = Authentication.Token.create(timeNow = timeNow)): StatelessAuthentication = StatelessAuthentication(token = token)