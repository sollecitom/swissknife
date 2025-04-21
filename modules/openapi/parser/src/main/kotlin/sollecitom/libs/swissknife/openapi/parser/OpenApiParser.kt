package sollecitom.libs.swissknife.openapi.parser

import sollecitom.libs.swissknife.openapi.parser.OpenApiParser.Companion.fullyResolvedParseOptions
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.toPath

interface OpenApiParser {

    fun parseContent(openApi: String): OpenAPI

    fun parse(openApiLocation: String, options: ParseOptions = fullyResolvedParseOptions()): OpenAPI

    class ParseException(val messages: List<String>) : RuntimeException(messages.joinToString(System.lineSeparator()))

    companion object {
        fun fullyResolvedParseOptions() = ParseOptions().apply {
            isResolve = true
            isResolveFully = true
            isResolveRequestBody = true
            isResolveCombinators = true
        }
    }
}

fun OpenApiParser.parse(openApiLocation: Path, options: ParseOptions = fullyResolvedParseOptions()) = parse(openApiLocation.toString(), options)

fun OpenApiParser.parse(validOpenApiUrl: URL) = parse(validOpenApiUrl.toURI().toPath().toString())