package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.kotlin.extensions.optional.asNullable
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.model.ResponseWithHeadersAdapter
import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.responses.ApiResponse
import org.http4k.core.ContentType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import io.swagger.v3.oas.models.media.Schema as SwaggerSchema

internal class ResponseJsonBodyValidator(val jsonSchemasDirectoryName: String = defaultJsonSchemasDirectory) : CustomResponseValidator {

    private val path = listOf("validation", "response", "body")

    override fun validate(rawResponse: Response, apiOperation: ApiOperation): ValidationReport {

        val response = (rawResponse as ResponseWithHeadersAdapter)
        val bodyAsString = response.responseBody.asNullable()?.toString(Charset.defaultCharset())
        val responseDefinition = apiOperation.operation.responses[response.status.toString()]
        val bodySwaggerSchema = responseDefinition?.content?.get(response.acceptHeader.withNoDirectives().toHeaderValue())?.schema
        val bodySchema = bodySwaggerSchema?.`$ref`?.resolveAsSchemaLocation()?.let { jsonSchemaAt(it) }
        return when {
            !bodyAsString.isNullOrEmpty() && bodySwaggerSchema.isDefined() && responseDefinition.declaresAJsonContentType() -> {
                val json = bodyAsString.toJsonValue()
                bodySwaggerSchema!!.asJsonSchema().validate(json).toValidationReport()
            }

            !bodyAsString.isNullOrEmpty() && !bodySwaggerSchema.isDefined() && responseDefinition.declaresAJsonContentType() -> {
                bodySchema ?: return ValidationReport.singleton(CustomValidation.message(RESPONSE_BODY_PATH, "Present but JSON schema is not declared"))
                val json = bodyAsString.toJsonValue()
                bodySchema.validate(json).toValidationReport()
            }

            else -> when {
                bodySchema != null -> ValidationReport.singleton(CustomValidation.message(RESPONSE_BODY_PATH, "Empty but JSON schema is declared"))
                else -> ValidationReport.empty()
            }
        }
    }

    private fun io.swagger.v3.oas.models.media.Schema<*>.asJsonSchema() = jsonObject(this).asSchema()

    private fun jsonObject(schema: io.swagger.v3.oas.models.media.Schema<*>): JSONObject {

        val node = Json.mapper().convertValue(schema, ObjectNode::class.java) // TODO this removes "const" from the schema for some reason - fix it when you have time
        return node.let(ObjectNode::toString).let(::JSONObject)
    }

    private fun SwaggerSchema<*>?.isDefined(): Boolean = this != null && ((properties != null && properties.isNotEmpty()) || (!oneOf.isNullOrEmpty()) || (!allOf.isNullOrEmpty()))

    private fun JsonSchema.ValidationFailure?.toValidationReport() = this?.let { ValidationReport.singleton(CustomValidation.message(it.fullPathAsString, it.message)) } ?: ValidationReport.empty()

    private fun ApiResponse?.declaresAJsonContentType() = this?.content?.keys?.any { it == ContentType.APPLICATION_JSON.value } ?: false

    private fun String.resolveAsSchemaLocation(): String = when {
        startsWith("#/components/schemas/") -> "${removePrefix("#/components/schemas/")}.json"
        else -> removePrefix("./$jsonSchemasDirectoryName/")
    }

    private fun String.toJsonValue(): JsonValue = try {
        JSONObject(this).let(JsonValue::Object)
    } catch (e: JSONException) {
        JSONArray(this).let(JsonValue::Array)
    }

    private fun JsonSchema.validate(json: JsonValue) = when (json) {
        is JsonValue.Array -> validate(json.value, path)
        is JsonValue.Object -> validate(json.value, path)
    }

    companion object {
        const val defaultJsonSchemasDirectory = "schemas/json"
        const val RESPONSE_BODY_PATH = "validation.response.body.schema"
    }
}