package sollecitom.libs.swissknife.correlation.core.domain.access.session

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider

data class FederatedSession(override val id: Id, val identityProvider: IdentityProvider) : Session {

    companion object
}