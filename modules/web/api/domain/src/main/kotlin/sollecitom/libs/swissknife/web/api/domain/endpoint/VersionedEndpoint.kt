package sollecitom.libs.swissknife.web.api.domain.endpoint

interface VersionedEndpoint : Endpoint {

    val endpointVersion: Int get() = 1
}