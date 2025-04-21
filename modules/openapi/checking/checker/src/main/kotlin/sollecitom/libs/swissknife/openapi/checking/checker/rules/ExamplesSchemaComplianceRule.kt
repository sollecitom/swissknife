package sollecitom.libs.swissknife.openapi.checking.checker.rules

import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import org.json.JSONObject
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import sollecitom.libs.swissknife.openapi.checking.checker.model.allResponses
import io.swagger.v3.oas.models.media.JsonSchema as SwaggerJsonSchema

// TODO doesn't work when 'const' is specified in the schemata (use 'enum' instead)
class ExamplesSchemaComplianceRule(private val mediaTypesToCheck: Set<String>, private val jsonSchemasDirectoryName: String = defaultJsonSchemasDirectory) : ComplianceRule<OpenAPI> {

    init {
        require(mediaTypesToCheck.isNotEmpty()) { "Must specify at least one media type name to check" }
    }

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().flatMap(::violations).toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private val OperationWithContext.requestBodyLocation: OpenApiLocation get() = OpenApiLocation(Location.RequestBody, this)

    private fun OperationWithContext.responseBodyLocation(statusCode: String): OpenApiLocation = OpenApiLocation(Location.ResponseBody(statusCode), this)

    private fun OperationWithContext.parameterLocation(parameter: Parameter): OpenApiLocation = OpenApiLocation(Location.Parameter(parameter), this)

    private fun violations(operation: OperationWithContext): Set<ComplianceRule.Result.Violation<OpenAPI>> {

        val violations = mutableSetOf<ComplianceRule.Result.Violation<OpenAPI>>()

        violations += operation.paramsViolations()
        violations += operation.requestBodyViolations()
        violations += operation.responseBodyViolations()

        return violations
    }

    private fun OperationWithContext.paramsViolations(): Set<ComplianceRule.Result.Violation<OpenAPI>> {

        val paramsWithSchema = parameters.filter { it.schema != null }
        val paramsWithJsonSchema = paramsWithSchema.filter { it.schema is SwaggerJsonSchema }
        val jsonParametersViolations = paramsWithJsonSchema.flatMap { parameter -> with(parameterLocation(parameter)) { parameter.jsonSchemaViolations() } }
        return jsonParametersViolations.toSet()
    }

    private fun OperationWithContext.requestBodyViolations(): Set<ComplianceRule.Result.Violation<OpenAPI>> = with(requestBodyLocation) {

        val requestBodyMediaTypes = requestBody.mediaTypes.filter { it.name in mediaTypesToCheck }
        requestBodyMediaTypes.flatMap { mediaType -> mediaType.violations() }.toSet()
    }

    private fun OperationWithContext.responseBodyViolations(): Set<ComplianceRule.Result.Violation<OpenAPI>> = operation.allResponses.flatMap { (statusCode, response) ->
        with(responseBodyLocation(statusCode)) {
            val mediaTypesToCheck = response.mediaTypes.filter { it.name in mediaTypesToCheck }.takeUnless { it.isEmpty() } ?: return@flatMap emptySet()
            mediaTypesToCheck.flatMap { mediaType -> mediaType.violations() }.toSet()
        }
    }.toSet()

    private data class OpenApiLocation(val location: Location, val operation: OperationWithContext)

    context(apiLocation: OpenApiLocation)
    private fun Parameter.jsonSchemaViolations() = examplesWithSchema.mapNotNull { example -> example.applicationJsonSchemaViolations() }.toSet()

    context(apiLocation: OpenApiLocation)
    private fun MediaTypeWithName.violations(): Set<ComplianceRule.Result.Violation<OpenAPI>> = when (name) {
        APPLICATION_JSON -> {
            examplesWithSchema.mapNotNull { example -> example.applicationJsonSchemaViolations() }.toSet()
        }

        else -> emptySet() // other media types aren't supported yet
    }

    context(apiLocation: OpenApiLocation)
    private fun ExampleInfo.applicationJsonSchemaViolations(): ComplianceRule.Result.Violation<OpenAPI>? {

        val jsonSchema = if (!schema.properties.isNullOrEmpty() || schema.oneOf != null || schema.allOf != null) {
            runCatching { schema.asJsonSchema() }.getOrElse { error -> return invalidJsonSchema(error, schema) }
        } else {
            val ref = schema.`$ref` ?: return noRefForSchema()
            val schemaLocation = ref.resolveAsSchemaLocation()
            runCatching { schemaLocation.let(::jsonSchemaAt) }.getOrElse { error -> return invalidJsonSchemaReference(error, schemaLocation) }
        }
        val jsonValue = runCatching { toJson(value) }.getOrElse { return invalidJsonViolation(value) }
        val path = validationPath + apiLocation.location.path
        val validationFailure = jsonSchema.validate(jsonValue, path)
        if (validationFailure != null) return incompatibleJsonSchemaViolation(validationFailure, jsonSchema)
        return null
    }

    private fun toJson(value: Any): JSONObject = when (value) {
        is Map<*, *> -> JSONObject(value)
        else -> JSONObject(value.toString())
    }

    context(apiLocation: OpenApiLocation, exampleInfo: ExampleInfo)
    private fun invalidJsonSchema(error: Throwable, schemaValue: Any) = InvalidJsonSchemaViolation(error, schemaValue, exampleInfo, apiLocation.location, apiLocation.operation)

    context(apiLocation: OpenApiLocation, exampleInfo: ExampleInfo)
    private fun noRefForSchema() = NoRefForSchemaViolation(exampleInfo, APPLICATION_JSON, apiLocation.location, apiLocation.operation)

    context(apiLocation: OpenApiLocation, exampleInfo: ExampleInfo)
    private fun invalidJsonSchemaReference(error: Throwable, schemaLocation: String) = InvalidJsonSchemaReferenceViolation(error, schemaLocation, exampleInfo, apiLocation.location, apiLocation.operation)

    context(apiLocation: OpenApiLocation, exampleInfo: ExampleInfo)
    private fun invalidJsonViolation(value: Any) = InvalidJsonViolation(value, exampleInfo, apiLocation.location, apiLocation.operation)

    context(apiLocation: OpenApiLocation, exampleInfo: ExampleInfo)
    private fun incompatibleJsonSchemaViolation(validationFailure: JsonSchema.ValidationFailure, jsonSchema: JsonSchema) = IncompatibleJsonSchemaViolation(validationFailure, jsonSchema, exampleInfo, apiLocation.location, apiLocation.operation)

    private fun Schema<*>.asJsonSchema() = jsonObject(this).asSchema()

    private fun jsonObject(schema: Schema<*>): JSONObject {

        val node = Json.mapper().convertValue(schema, ObjectNode::class.java) // TODO this removes "const" from the schema for some reason - fix it when you have time
        return node.let(ObjectNode::toString).let(::JSONObject)
    }

    private val Parameter.examplesWithSchema: List<ExampleInfo>
        get() = buildList {
            val examples = schema?.let { schema -> examples.orEmpty().map { ExampleInfo(it.key, it.value.value, schema) } }.orEmpty()
            addAll(examples)

            val example = schema?.let { schema -> example?.let { ExampleInfo("<unnamed single example>", it, schema) } }
            if (example != null) {
                add(example)
            }
        }

    private val MediaTypeWithName.examplesWithSchema: List<ExampleInfo>
        get() = buildList {
            val examples = value.schema?.let { schema -> value.examples.orEmpty().map { ExampleInfo(it.key, it.value.value, schema) } }.orEmpty()
            addAll(examples)

            val example = value.schema?.let { schema -> value.example?.let { ExampleInfo("<unnamed single example>", it, schema) } }
            if (example != null) {
                add(example)
            }
        }

    private fun String.resolveAsSchemaLocation(): String = when {
        startsWith("#/components/schemas/") -> "${removePrefix("#/components/schemas/")}.json"
        else -> removePrefix("./$jsonSchemasDirectoryName/")
    }

    private val RequestBody?.mediaTypes: List<MediaTypeWithName> get() = this?.content?.mediaTypes.orEmpty()
    private val ApiResponse?.mediaTypes: List<MediaTypeWithName> get() = this?.content?.mediaTypes.orEmpty()
    private val Content?.mediaTypes: List<MediaTypeWithName> get() = this?.takeUnless(Content::isEmpty)?.entries?.map { MediaTypeWithName(it.key, it.value) }.orEmpty()

    data class MediaTypeWithName(val name: String, val value: MediaType)

    sealed interface Location {

        val path: List<String>
        val description: String

        data object RequestBody : Location {
            override val path = listOf("request")
            override val description = "<request body>"
        }

        data class ResponseBody(val statusCode: String) : Location {
            override val path = listOf("response")
            override val description = "<response body for status code '$statusCode'>"
        }

        data class Parameter(val parameter: io.swagger.v3.oas.models.parameters.Parameter) : Location {
            override val path = listOf("parameter", parameter.name)
            override val description = "<parameter with name '${parameter.name}'>"
        }
    }

    data class ExampleInfo(val name: String, val value: Any, val schema: Schema<*>)

    class NoRefForSchemaViolation(val example: ExampleInfo, val mediaTypeName: String, val location: Location, val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} has an example with name '${example.name}' in ${location.description} with a $mediaTypeName schema that has no '\$ref'"
    }

    class InvalidJsonSchemaViolation(val error: Throwable, val schemaValue: Any, val example: ExampleInfo, val location: Location, val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} has an example with name '${example.name}' in ${location.description} that specifies an $APPLICATION_JSON schema but the schema could not be loaded. The value is '${schemaValue}', and the error was: ${error.message}"
    }

    class InvalidJsonSchemaReferenceViolation(val error: Throwable, val schemaLocation: String, val example: ExampleInfo, val location: Location, val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} has an example with name '${example.name}' in ${location.description} that specifies an $APPLICATION_JSON schema at $schemaLocation, but the schema could not be loaded. The error was: ${error.message}"
    }

    class IncompatibleJsonSchemaViolation(val failure: JsonSchema.ValidationFailure, val schema: JsonSchema, val example: ExampleInfo, val location: Location, val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} has an example with name '${example.name}' in ${location.description} that doesn't comply with its $APPLICATION_JSON schema '${schema.description}'. The error was: ${failure.message}"
    }

    class InvalidJsonViolation(val value: Any, val example: ExampleInfo, val location: Location, val operation: OperationWithContext) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} has an example with name '${example.name}' in ${location.description} with a non-JSON value and a declared $APPLICATION_JSON schema. The value is: '$value'"
    }

    companion object {
        const val defaultJsonSchemasDirectory = "schemas/json"
        private const val APPLICATION_JSON = "application/json"

        private val validationPath = listOf("validation")
    }
}