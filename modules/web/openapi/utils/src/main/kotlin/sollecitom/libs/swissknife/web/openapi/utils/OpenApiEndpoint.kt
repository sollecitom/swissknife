package sollecitom.libs.swissknife.web.openapi.utils

import sollecitom.libs.swissknife.http4k.utils.contentType
import sollecitom.libs.swissknife.openapi.provider.provider.LocationBasedOpenApiProvider
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint
import io.swagger.v3.core.util.Json
import io.swagger.v3.core.util.Yaml
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.accept
import org.http4k.routing.bind

class OpenApiEndpoint(openApiLocation: String) : Endpoint {

    private val openApiYamlDefinition = LocationBasedOpenApiProvider(openApiLocation).rawOpenApi
    private val openApiJsonDefinition = Json.pretty().writeValueAsString(Yaml.mapper().readTree(openApiYamlDefinition))
    override val path: String get() = "/api"
    override val methods: Set<Method> = setOf(Method.GET)
    override val route = path bind Method.GET to { request ->

        val contentType = request.accept()?.contentTypes?.map { it.content }?.firstNotNullOfOrNull { requested -> supportedContentTypes.firstOrNull { supported -> supported.equalsIgnoringDirectives(requested) } } ?: defaultContentType
        Response(OK).body(contentType.representation()).contentType(contentType)
    }

    private fun ContentType.representation(): String = when {
        equalsIgnoringDirectives(ContentType.APPLICATION_YAML) -> openApiYamlDefinition
        equalsIgnoringDirectives(ContentType.APPLICATION_JSON) -> openApiJsonDefinition
        else -> error("Unsupported content type $this")
    }

    companion object {
        private val defaultContentType = ContentType.APPLICATION_YAML
        private val supportedContentTypes = setOf(ContentType.APPLICATION_YAML, ContentType.APPLICATION_JSON)
    }
}