package sollecitom.libs.swissknife.correlation.core.domain.trace

import sollecitom.libs.swissknife.core.domain.identity.Id
import kotlinx.datetime.Instant

data class InvocationTrace(val id: Id, val createdAt: Instant) {

    companion object
}