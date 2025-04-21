package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method

interface CommandEndpoint : VersionedEndpoint {

    val commandName: String
    override val methods get() = setOf(Method.POST)

    override val path get() = "/commands/$commandName/v${endpointVersion}"
}