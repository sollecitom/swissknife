package sollecitom.libs.swissknife.openapi.provider.definition.reader

import sollecitom.libs.swissknife.core.domain.version.Version
import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import sollecitom.libs.swissknife.openapi.provider.definition.OpenApiDefinition
import sollecitom.libs.swissknife.openapi.provider.provider.LocationBasedOpenApiProvider
import io.swagger.v3.parser.core.models.ParseOptions

// TODO remove this whole thing
val OpenApiDefinitionReader.Companion.standard: OpenApiDefinitionReader get() = StandardOpenApiDefinitionReader

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

// TODO remove this and read the normal 3.1 API
internal object DowngradingOpenApiDefinitionReader : OpenApiDefinitionReader {

    private val maximumVersion = Version.Semantic(3, 0, 0)
    private val versionField = Regex("""((?:^|[{,])\s*["']?openapi["']?\s*:\s*)(["']?)([0-9]+\.[0-9]+\.[0-9]+)(["']?)""", RegexOption.MULTILINE)

    override fun read(openApiLocation: String): OpenApiDefinition {

        val rawContent = LocationBasedOpenApiProvider(openApiLocation).rawOpenApi
        val version = versionField.find(rawContent)?.groupValues?.get(3)?.let(Version.Semantic::parse) ?: return StandardOpenApiDefinitionReader.read(openApiLocation)
        if (version <= maximumVersion) return StandardOpenApiDefinitionReader.read(openApiLocation)
        val downgradedContent = rawContent.replaceFirst(versionField, "$1$2${maximumVersion.value.value}$4")
        val parsedOpenApi = OpenApiReader.parseContent(openApi = downgradedContent, baseLocation = openApiLocation, options = parseOptions)
        return ResolvedOpenApiDefinition(api = parsedOpenApi)
    }
}
