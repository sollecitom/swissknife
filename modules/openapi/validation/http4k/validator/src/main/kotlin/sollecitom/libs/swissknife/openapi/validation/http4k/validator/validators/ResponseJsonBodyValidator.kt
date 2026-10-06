package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import com.atlassian.oai.validator.util.ContentTypeUtils
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import kotlin.jvm.optionals.getOrNull
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.model.ResponseWithHeadersAdapter
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils.responseFor
import sollecitom.libs.swissknife.openapi.validation.request.validator.ValidationReportError
import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.v3.core.util.Json
import org.json.JSONArray
import org.json.JSONObject
import io.swagger.v3.oas.models.media.Schema as SwaggerSchema

internal class ResponseJsonBodyValidator(val jsonSchemasDirectoryName: String = defaultJsonSchemasDirectory) : CustomResponseValidator {

    private val path = listOf("validation", "response", "body")

    override fun validate(rawResponse: Response, apiOperation: ApiOperation): ValidationReport {

        val response = (rawResponse as ResponseWithHeadersAdapter)
        val bodyAsString = response.responseBody.getOrNull()?.toString(Charsets.UTF_8)
        val responseContent = apiOperation.responseFor(response.status)?.content
        val declaredMediaType = responseContent?.keys?.let { ContentTypeUtils.findMostSpecificMatch(response.mediaType(), it).getOrNull() }
        val bodySwaggerSchema = declaredMediaType?.let { responseContent[it] }?.schema
        val bodySchema = bodySwaggerSchema?.`$ref`?.resolveAsSchemaLocation()?.let { jsonSchemaAt(it) }
        val declaresAJsonContentType = declaredMediaType != null && ContentTypeUtils.isJsonContentType(declaredMediaType)
        return when {
            !bodyAsString.isNullOrEmpty() && bodySwaggerSchema.isDefined() && declaresAJsonContentType -> {
                val json = bodyAsString.toJsonValue() ?: return invalidJson()
                bodySwaggerSchema!!.asJsonSchema().validate(json).toValidationReport()
            }

            !bodyAsString.isNullOrEmpty() && !bodySwaggerSchema.isDefined() && declaresAJsonContentType -> {
                bodySchema ?: return ValidationReport.singleton(CustomValidation.message(RESPONSE_BODY_PATH, "Present but JSON schema is not declared"))
                val json = bodyAsString.toJsonValue() ?: return invalidJson()
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

    private fun ResponseWithHeadersAdapter.mediaType(): String = contentType.getOrNull() ?: acceptHeader.withNoDirectives().toHeaderValue()

    private fun invalidJson() = ValidationReport.singleton(CustomValidation.message(ValidationReportError.Response.InvalidJson.key, "Present but not valid JSON"))

    private fun String.resolveAsSchemaLocation(): String = when {
        startsWith("#/components/schemas/") -> "${removePrefix("#/components/schemas/")}.json"
        else -> removePrefix("./$jsonSchemasDirectoryName/")
    }

    private fun String.toJsonValue(): JsonValue? = runCatching<JsonValue> { JSONObject(this).let(JsonValue::Object) }.recoverCatching { JSONArray(this).let(JsonValue::Array) }.getOrNull()

    private fun JsonSchema.validate(json: JsonValue) = when (json) {
        is JsonValue.Array -> validate(json.value, path)
        is JsonValue.Object -> validate(json.value, path)
    }

    companion object {
        const val defaultJsonSchemasDirectory = "schemas/json"
        const val RESPONSE_BODY_PATH = "validation.response.body.schema"
    }
}