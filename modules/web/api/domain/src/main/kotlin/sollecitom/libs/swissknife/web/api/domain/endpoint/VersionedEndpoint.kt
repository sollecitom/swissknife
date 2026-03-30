package sollecitom.libs.swissknife.web.api.domain.endpoint

/** An [Endpoint] with a version number, defaulting to 1. */
interface VersionedEndpoint : Endpoint {

    val endpointVersion: Int get() = 1
}