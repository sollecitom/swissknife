package sollecitom.libs.swissknife.correlation.core.domain.trace

import sollecitom.libs.swissknife.core.domain.identity.Id

/** An externally-visible trace with an invocation ID (unique per call) and an action ID (groups related calls). */
data class ExternalInvocationTrace(val invocationId: Id, val actionId: Id) {

    companion object
}