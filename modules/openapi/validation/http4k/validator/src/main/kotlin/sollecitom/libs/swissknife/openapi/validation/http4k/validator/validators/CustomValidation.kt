package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.report.ValidationReport

internal object CustomValidation {

    private const val ADDITIONAL_INFO_MARKER = "CUSTOM_VALIDATION_CONTEXT_MARKER"

    fun message(key: String, value: String): ValidationReport.Message = ValidationReport.Message.create(key, value).withAdditionalInfo(ADDITIONAL_INFO_MARKER).build()

    fun produced(message: ValidationReport.Message): Boolean = ADDITIONAL_INFO_MARKER in message.additionalInfo
}