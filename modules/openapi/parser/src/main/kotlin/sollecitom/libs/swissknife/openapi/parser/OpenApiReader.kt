package sollecitom.libs.swissknife.openapi.parser

import sollecitom.libs.swissknife.openapi.parser.OpenApiValidator.Result.Invalid
import sollecitom.libs.swissknife.openapi.parser.OpenApiValidator.Result.Valid
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.parser.core.models.SwaggerParseResult

object OpenApiReader : OpenApiValidator, OpenApiParser {

    override fun validate(openApiLocation: String) = loadOpenApi(openApiLocation, OpenApiParser.fullyResolvedParseOptions()).asValidatorResult()

    override fun parseContent(openApi: String): OpenAPI = readOpenApi(openApi).getOrThrow()

    override fun parse(openApiLocation: String, options: ParseOptions): OpenAPI = loadOpenApi(openApiLocation, options).getOrThrow()

    private fun loadOpenApi(openApiLocation: String, options: ParseOptions): SwaggerParseResult = OpenAPIV3Parser().readLocation(openApiLocation, emptyList(), options)

    private fun SwaggerParseResult.asValidatorResult(): OpenApiValidator.Result = if (messages.isEmpty()) Valid else Invalid(messages.map(OpenApiValidator::Error).toSet())

    private fun readOpenApi(openApi: String) = OpenAPIV3Parser().readContents(openApi)

    private fun SwaggerParseResult.getOrThrow(): OpenAPI = takeIf { messages.isEmpty() }?.openAPI ?: throw OpenApiParser.ParseException(messages)
}

