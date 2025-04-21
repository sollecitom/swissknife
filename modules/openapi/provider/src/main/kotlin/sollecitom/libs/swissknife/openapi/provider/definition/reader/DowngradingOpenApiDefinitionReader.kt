package sollecitom.libs.swissknife.openapi.provider.definition.reader

import sollecitom.libs.swissknife.core.domain.version.Version
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import sollecitom.libs.swissknife.openapi.provider.definition.OpenApiDefinition
import sollecitom.libs.swissknife.openapi.provider.provider.LocationBasedOpenApiProvider
import sollecitom.libs.swissknife.openapi.provider.provider.OpenApiProvider
import io.swagger.v3.parser.core.models.ParseOptions
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeText

// TODO remove this whole thing
val OpenApiDefinitionReader.Companion.standard: OpenApiDefinitionReader get() = StandardOpenApiDefinitionReader

internal object StandardOpenApiDefinitionReader : OpenApiDefinitionReader, Loggable() {

    private val parseOptions = ParseOptions().apply {
        isResolve = true
        isResolveFully = false
        isResolveRequestBody = false
        isResolveCombinators = false
    }

    override fun read(openApiLocation: String): OpenApiDefinition {

        val parsedOpenApi = OpenApiReader.parse(openApiLocation = openApiLocation, options = parseOptions)
        return ResolvedOpenApiDefinition(api = parsedOpenApi)
    }
}

// TODO remove this and read the normal 3.1 API
internal object DowngradingOpenApiDefinitionReader : OpenApiDefinitionReader, Loggable() {

    private val temporaryDirectory: Path by lazy { Files.createTempDirectory("open-api").apply { toFile().deleteOnExit() } }
    private val maximumVersion = Version.Semantic(3, 0, 0)
    private val parseOptions = ParseOptions().apply {
        isResolve = true
        isResolveFully = false
        isResolveRequestBody = false
        isResolveCombinators = false
    }

    override fun read(openApiLocation: String): OpenApiDefinition {

        val provider = LocationBasedOpenApiProvider(openApiLocation)
        val (rawContent, version) = provider.rawContentAndVersion()
        val definitionLocation = when {
            version > maximumVersion -> downgradedOpenApiDefinitionPath(rawContent = rawContent, version = version, downgradedVersion = maximumVersion).toString()
            else -> openApiLocation
        }
        val parsedOpenApi = OpenApiReader.parse(openApiLocation = definitionLocation, options = parseOptions)
        return ResolvedOpenApiDefinition(api = parsedOpenApi)
    }

    private fun downgradedOpenApiDefinitionPath(rawContent: String, version: Version.Semantic, downgradedVersion: Version.Semantic): Path {

        // It downgrades the OpenAPI version from 3.1.0 or above to 3.0.0, as somehow the type information is lost otherwise, so the clients cannot generate an SDK.
        val contentWithDowngradedVersion = rawContent.replace("openapi: ${version.value.value}", "openapi: ${downgradedVersion.value.value}")
        val temporaryDowngradedApiFile = Files.createTempFile(temporaryDirectory, "downgraded-api", ".yaml").apply { toFile().deleteOnExit() }
        temporaryDowngradedApiFile.writeText(contentWithDowngradedVersion)
        logger.info { "Saved downgraded OpenAPI definition in a temporary file with path '${temporaryDowngradedApiFile.toAbsolutePath()}'. This file and its parent directory will be deleted when the process exits." }
        return temporaryDowngradedApiFile.toAbsolutePath()
    }

    private fun OpenApiProvider.rawContentAndVersion(): Pair<String, Version.Semantic> {

        val rawVersion = openApi.openapi
        val version = Version.Semantic.parse(rawVersion)
        return rawOpenApi to version
    }
}