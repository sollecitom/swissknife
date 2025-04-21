package sollecitom.libs.swissknife.correlation.core.domain.access.session

import sollecitom.libs.swissknife.core.domain.identity.Id

@JvmInline
value class SimpleSession(override val id: Id) : Session {

    companion object
}