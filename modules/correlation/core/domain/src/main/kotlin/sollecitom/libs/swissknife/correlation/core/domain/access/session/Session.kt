package sollecitom.libs.swissknife.correlation.core.domain.access.session

import sollecitom.libs.swissknife.core.domain.identity.Id

sealed interface Session {

    val id: Id

    companion object
}