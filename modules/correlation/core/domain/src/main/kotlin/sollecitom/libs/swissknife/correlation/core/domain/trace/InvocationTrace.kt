package sollecitom.libs.swissknife.correlation.core.domain.trace

import sollecitom.libs.swissknife.core.domain.identity.Id
import kotlin.time.Instant

/** Identifies a single invocation in the trace chain with a unique ID and creation timestamp. */
data class InvocationTrace(val id: Id, val createdAt: Instant) {

    companion object
}