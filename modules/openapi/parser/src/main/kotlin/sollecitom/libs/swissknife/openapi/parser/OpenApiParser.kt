package sollecitom.libs.swissknife.openapi.parser

import sollecitom.libs.swissknife.openapi.parser.OpenApiParser.Companion.fullyResolvedParseOptions
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import java.net.URL
import java.nio.file.Path

interface OpenApiParser {

    /**
     * Parses [openApi] content. Relative `$ref`s are resolved against [baseLocation] (the file or URL the content came from);
     * without it, content has no location, so only absolute or in-document references resolve.
     */
    fun parseContent(openApi: String, baseLocation: String? = null, options: ParseOptions = referenceResolvingParseOptions()): OpenAPI

    fun parse(openApiLocation: String, options: ParseOptions = fullyResolvedParseOptions()): OpenAPI

    class ParseException(val messages: List<String>) : RuntimeException(messages.joinToString(System.lineSeparator()))

    companion object {
        fun referenceResolvingParseOptions() = ParseOptions().apply { isResolve = true }

        fun fullyResolvedParseOptions() = ParseOptions().apply {
            isResolve = true
            isResolveFully = true
            isResolveRequestBody = true
            isResolveCombinators = true
        }
    }
}

fun OpenApiParser.parse(openApiLocation: Path, options: ParseOptions = fullyResolvedParseOptions()) = parse(openApiLocation.toString(), options)

fun OpenApiParser.parse(validOpenApiUrl: URL) = parse(validOpenApiUrl.toString())