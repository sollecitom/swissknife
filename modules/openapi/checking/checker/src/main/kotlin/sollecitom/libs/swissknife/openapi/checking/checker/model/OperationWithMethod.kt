package sollecitom.libs.swissknife.openapi.checking.checker.model

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponses

data class OperationWithMethod(val operation: Operation, val method: PathItem.HttpMethod, val pathParameters: List<Parameter> = emptyList()) {

    val parameters: List<Parameter> = operation.parameters.orEmpty().let { operationParameters -> operationParameters + pathParameters.filterNot { it.isOverriddenBy(operationParameters) } }
    val requestBody: RequestBody? get() = operation.requestBody
    val responses: ApiResponses? get() = operation.responses

    private fun Parameter.isOverriddenBy(operationParameters: List<Parameter>) = operationParameters.any { it.name == name && it.`in` == `in` }
}

val OperationWithMethod.allResponses get() = responses?.takeUnless(ApiResponses::isEmpty).orEmpty()

internal fun PathItem.operations(pathName: String): Set<OperationWithContext> = operations().asSequence().map { OperationWithContext(it, pathName) }.toSet()

internal fun PathItem.operations(): Set<OperationWithMethod> {

    val pathParameters = parameters.orEmpty()
    val allOperations = mutableSetOf<OperationWithMethod>()

    if (get != null) {
        allOperations.add(OperationWithMethod(get, PathItem.HttpMethod.GET, pathParameters))
    }
    if (put != null) {
        allOperations.add(OperationWithMethod(put, PathItem.HttpMethod.PUT, pathParameters))
    }
    if (head != null) {
        allOperations.add(OperationWithMethod(head, PathItem.HttpMethod.HEAD, pathParameters))
    }
    if (post != null) {
        allOperations.add(OperationWithMethod(post, PathItem.HttpMethod.POST, pathParameters))
    }
    if (delete != null) {
        allOperations.add(OperationWithMethod(delete, PathItem.HttpMethod.DELETE, pathParameters))
    }
    if (patch != null) {
        allOperations.add(OperationWithMethod(patch, PathItem.HttpMethod.PATCH, pathParameters))
    }
    if (options != null) {
        allOperations.add(OperationWithMethod(options, PathItem.HttpMethod.OPTIONS, pathParameters))
    }
    if (trace != null) {
        allOperations.add(OperationWithMethod(trace, PathItem.HttpMethod.TRACE, pathParameters))
    }

    return allOperations
}