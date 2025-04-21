package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method

interface QueryEndpoint : VersionedEndpoint {

    val queryName: String
    override val methods get() = setOf(Method.POST)

    override val path get() = "/queries/$queryName/v${endpointVersion}"
}