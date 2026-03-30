package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method

/** A POST endpoint following the event pattern at `/events/{eventName}/v{version}`. */
interface EventEndpoint : VersionedEndpoint {

    val eventName: String
    override val methods get() = setOf(Method.POST)

    override val path get() = "/events/$eventName/v${endpointVersion}"
}