package sollecitom.libs.swissknife.web.api.test.utils

interface QueryEndpointHttpTestSpecification : EndpointTestSpecification {

    val queryName: String
    override val pathWithoutVersion: String get() = "queries/$queryName"
}

