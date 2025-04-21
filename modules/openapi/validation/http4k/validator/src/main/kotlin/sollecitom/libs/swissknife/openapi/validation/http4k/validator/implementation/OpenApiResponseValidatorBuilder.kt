package sollecitom.libs.swissknife.openapi.validation.http4k.validator.implementation

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import com.atlassian.oai.validator.report.ValidationReport.Message
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators.CustomValidation
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators.UnknownHeadersRejectingResponseValidator
import io.swagger.v3.oas.models.OpenAPI

internal interface OpenApiResponseValidatorBuilder {

    fun withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders: Boolean): OpenApiResponseValidatorBuilder

    fun withCustomResponseValidation(responseJsonBodyValidator: CustomResponseValidator): OpenApiResponseValidatorBuilder

    fun build(): OpenApiResponseValidator
}

internal fun OpenApiResponseValidator.Companion.createFor(openApi: OpenAPI): OpenApiResponseValidatorBuilder = CustomizedOpenApiResponseValidatorBuilder(openApi)

private class CustomizedOpenApiResponseValidatorBuilder(openApi: OpenAPI) : OpenApiResponseValidatorBuilder {

    private val builder = OpenApiInteractionValidator.createFor(openApi)

    override fun withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders: Boolean): OpenApiResponseValidatorBuilder {

        if (rejectUnknownResponseHeaders) {
            builder.withCustomResponseValidation(UnknownHeadersRejectingResponseValidator)
        }
        return this
    }

    override fun withCustomResponseValidation(responseJsonBodyValidator: CustomResponseValidator): OpenApiResponseValidatorBuilder {

        builder.withCustomResponseValidation(responseJsonBodyValidator)
        return this
    }

    override fun build(): OpenApiResponseValidator = InterceptingOpenApiResponseValidator(builder.build())

    private class InterceptingOpenApiResponseValidator(private val delegate: OpenApiInteractionValidator) : OpenApiResponseValidator {

        override fun validateResponse(path: String, method: Request.Method, response: Response): ValidationReport {

            val messages = delegate.validateResponse(path, method, response).messages
            return ValidationReport.from(messages.filterOutStandardResponseJsonSchemaErrors())
        }

        private fun List<Message>.filterOutStandardResponseJsonSchemaErrors() = filterNot { it.key.startsWith(responseBodySchemaPath) && !CustomValidation.produced(it) }

        companion object {
            private const val responseBodySchemaPath = "validation.response.body.schema"
        }
    }
}