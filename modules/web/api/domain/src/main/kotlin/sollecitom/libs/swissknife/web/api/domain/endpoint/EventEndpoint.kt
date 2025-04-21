package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method

interface EventEndpoint : VersionedEndpoint {

    val eventName: String
    override val methods get() = setOf(Method.POST)

    override val path get() = "/events/$eventName/v${endpointVersion}"
}