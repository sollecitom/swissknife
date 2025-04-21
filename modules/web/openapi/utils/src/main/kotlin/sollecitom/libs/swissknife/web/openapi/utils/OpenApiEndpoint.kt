package sollecitom.libs.swissknife.web.openapi.utils

import sollecitom.libs.swissknife.http4k.utils.contentType
import sollecitom.libs.swissknife.openapi.provider.definition.reader.OpenApiDefinitionReader
import sollecitom.libs.swissknife.openapi.provider.definition.reader.standard
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint
import io.swagger.v3.parser.core.models.ParseOptions
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.accept
import org.http4k.routing.bind

class OpenApiEndpoint(private val openApiLocation: String) : Endpoint {

    private val openApi by lazy { OpenApiDefinitionReader.standard.read(openApiLocation) }
    private val openApiYamlDefinition by lazy { openApi.asYaml }
    private val openApiJsonDefinition by lazy { openApi.asJson }
    private val byContentType = mutableMapOf<ContentType, String>()
    override val path: String get() = "/api"
    override val methods: Set<Method> = setOf(Method.GET)
    override val route = path bind Method.GET to { request ->

        val contentType = request.accept()?.contentTypes?.map { it.content }?.firstOrNull { supportedContentTypes.any { supported -> supported.equalsIgnoringDirectives(it) } } ?: defaultContentType
        val body = byContentType.getOrPut(contentType) { contentType.representation() }
        Response(OK).body(body).contentType(contentType)
    }

    private fun ContentType.representation(): String = when {
        equalsIgnoringDirectives(ContentType.APPLICATION_YAML) -> openApiYamlDefinition
        equalsIgnoringDirectives(ContentType.APPLICATION_JSON) -> openApiJsonDefinition
        else -> error("Unsupported content type $this")
    }

    companion object {
        private val defaultContentType = ContentType.APPLICATION_YAML
        private val supportedContentTypes = setOf(ContentType.APPLICATION_YAML, ContentType.APPLICATION_JSON)

        private val parseOptions = ParseOptions().apply {
            isResolve = true
            isResolveFully = false
            isResolveRequestBody = false
            isResolveCombinators = false
        }
    }
}