package sollecitom.libs.swissknife.correlation.core.domain.trace

import sollecitom.libs.swissknife.core.domain.identity.Id

data class ExternalInvocationTrace(val invocationId: Id, val actionId: Id) {

    companion object
}