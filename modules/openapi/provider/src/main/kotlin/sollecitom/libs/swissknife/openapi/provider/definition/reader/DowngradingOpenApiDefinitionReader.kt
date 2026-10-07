package sollecitom.libs.swissknife.openapi.provider.definition.reader

import sollecitom.libs.swissknife.core.domain.version.Version
import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import sollecitom.libs.swissknife.openapi.provider.definition.OpenApiDefinition
import sollecitom.libs.swissknife.openapi.provider.provider.LocationBasedOpenApiProvider
import io.swagger.v3.parser.core.models.ParseOptions

val OpenApiDefinitionReader.Companion.standard: OpenApiDefinitionReader get() = StandardOpenApiDefinitionReader

/** Reads definitions above OpenAPI 3.0.0 as 3.0.0, since some SDK generators lose type information otherwise. Relative `$ref`s still resolve against the original location. */
val OpenApiDefinitionReader.Companion.downgrading: OpenApiDefinitionReader get() = DowngradingOpenApiDefinitionReader

private val parseOptions = ParseOptions().apply {
    isResolve = true
    isResolveFully = false
    isResolveRequestBody = false
    isResolveCombinators = false
}

internal object StandardOpenApiDefinitionReader : OpenApiDefinitionReader {

    override fun read(openApiLocation: String): OpenApiDefinition {

        val parsedOpenApi = OpenApiReader.parse(openApiLocation = openApiLocation, options = parseOptions)
        return ResolvedOpenApiDefinition(api = parsedOpenApi)
    }
}

internal object DowngradingOpenApiDefinitionReader : OpenApiDefinitionReader {

    private val maximumVersion = Version.Semantic(3, 0, 0)
    private val versionLine = Regex("""^(openapi:\s*)(["']?)([0-9]+\.[0-9]+\.[0-9]+)(["']?)\s*$""", RegexOption.MULTILINE)

    override fun read(openApiLocation: String): OpenApiDefinition {

        val rawContent = LocationBasedOpenApiProvider(openApiLocation).rawOpenApi
        val version = versionLine.find(rawContent)?.groupValues?.get(3)?.let(Version.Semantic::parse) ?: return StandardOpenApiDefinitionReader.read(openApiLocation)
        if (version <= maximumVersion) return StandardOpenApiDefinitionReader.read(openApiLocation)
        val downgradedContent = rawContent.replaceFirst(versionLine, "$1$2${maximumVersion.value.value}$4")
        val parsedOpenApi = OpenApiReader.parseContent(openApi = downgradedContent, baseLocation = openApiLocation, options = parseOptions)
        return ResolvedOpenApiDefinition(api = parsedOpenApi)
    }
}
