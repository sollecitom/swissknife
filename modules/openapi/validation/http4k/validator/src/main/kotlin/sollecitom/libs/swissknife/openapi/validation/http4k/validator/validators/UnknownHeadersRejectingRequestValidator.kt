package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.request.CustomRequestValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.report.ValidationReport
import sollecitom.libs.swissknife.http4k.utils.HttpHeaders
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils.inHeader
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils.parameters
import sollecitom.libs.swissknife.openapi.validation.request.validator.ValidationReportError
import io.swagger.v3.oas.models.parameters.Parameter

internal object UnknownHeadersRejectingRequestValidator : CustomRequestValidator {

    private val whitelistedUnknownHeaderNames = setOf(HttpHeaders.ContentType.name.lowercase())

    override fun validate(request: Request, apiOperation: ApiOperation): ValidationReport {

        val operationHeaders = apiOperation.parameters().inHeader().toSet()
        val unknownHeaderNames = request.headers.notDeclaredIn(operationHeaders)
        return if (unknownHeaderNames.isNotEmpty()) ValidationReport.from(unknownHeaderNames.map { CustomValidation.message(ValidationReportError.Request.UnknownHeader.key, "Unknown request headers ${unknownHeaderNames.joinToString(separator = ",", prefix = "[", postfix = "]")}") }) else ValidationReport.empty()
    }

    private fun Map<String, Collection<String>>.notDeclaredIn(knownHeaders: Set<Parameter>): Set<String> {

        val knownHeaderNames = knownHeaders.map { it.name.lowercase() }.toSet()
        return keys.filterNot { it.lowercase() in whitelistedUnknownHeaderNames }.filterNot { headerName -> headerName.lowercase() in knownHeaderNames }.toSet()
    }
}