package sollecitom.libs.swissknife.web.api.test.utils

interface EventEndpointHttpTestSpecification : EndpointTestSpecification {

    val eventName: String
    override val pathWithoutVersion: String get() = "events/$eventName"
}

