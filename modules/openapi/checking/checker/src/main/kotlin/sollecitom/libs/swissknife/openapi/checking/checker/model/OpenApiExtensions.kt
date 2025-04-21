package sollecitom.libs.swissknife.openapi.checking.checker.model

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponses

fun OpenAPI.allParameters(): Set<ParameterWithLocation> = paths.asSequence().flatMap { (pathName, path) -> path.allParameters(pathName) }.toSet()

fun OpenAPI.allOperations(): Set<OperationWithContext> = paths.asSequence().flatMap { (pathName, path) -> path.operations(pathName) }.toSet()

private fun PathItem.allParameters(pathName: String): Set<ParameterWithLocation> = operations().asSequence().flatMap { operation -> operation.allParameters(pathName) }.toSet()

private fun OperationWithMethod.allParameters(pathName: String): Set<ParameterWithLocation> = parameters.asSequence().map { parameter -> ParameterWithLocation(parameter, parameter.location(pathName, this)) }.toSet()

private fun Parameter.location(pathName: String, operation: OperationWithMethod) = ParameterLocation.valueOf(pathName = pathName, operation = operation, value = `in`)

data class OperationWithContext(val operation: OperationWithMethod, val pathName: String) {

    val summary: String? get() = operation.operation.summary
    val description: String? get() = operation.operation.description
    val method: PathItem.HttpMethod get() = operation.method
    val operationId: String? get() = operation.operation.operationId
    val requestBody: RequestBody? = operation.requestBody
    val responses: ApiResponses? = operation.responses
    val parameters: List<Parameter> get() = operation.parameters
}

fun RequestBody.isRequired(): Boolean = required ?: false