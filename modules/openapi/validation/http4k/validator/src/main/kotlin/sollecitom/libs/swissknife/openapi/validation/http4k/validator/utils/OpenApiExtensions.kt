package sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils

import com.atlassian.oai.validator.model.ApiOperation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.responses.ApiResponse

internal fun ApiOperation.parameters(openApi: OpenAPI): List<Parameter> = operation.parameters.orEmpty() + openApi.paths?.get(apiPath.original())?.parameters.orEmpty()

internal fun List<Parameter>.inHeader(): List<Parameter> = filter { it.`in` == "header" }

internal fun List<Parameter>.inQuery(): List<Parameter> = filter { it.`in` == "query" }

internal fun ApiOperation.responseFor(status: Int): ApiResponse? = operation.responses?.let { responses -> responses[status.toString()] ?: responses["${status / 100}XX"] ?: responses["default"] }