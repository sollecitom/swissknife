package sollecitom.libs.swissknife.web.api.test.utils

interface CommandEndpointHttpTestSpecification : EndpointTestSpecification {

    val commandName: String
    override val pathWithoutVersion: String get() = "commands/$commandName"
}