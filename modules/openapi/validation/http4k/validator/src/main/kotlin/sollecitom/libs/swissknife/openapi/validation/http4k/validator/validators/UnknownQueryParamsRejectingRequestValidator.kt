package sollecitom.libs.swissknife.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.request.CustomRequestValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.report.ValidationReport
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils.inQuery
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.utils.parameters
import sollecitom.libs.swissknife.openapi.validation.request.validator.ValidationReportError
import io.swagger.v3.oas.models.parameters.Parameter

internal object UnknownQueryParamsRejectingRequestValidator : CustomRequestValidator {

    override fun validate(request: Request, apiOperation: ApiOperation): ValidationReport {

        val operationQueryParams = apiOperation.parameters().inQuery().toSet()
        val unknownHeaderNames = request.queryParameters.notDeclaredIn(operationQueryParams)
        return if (unknownHeaderNames.isNotEmpty()) ValidationReport.from(unknownHeaderNames.map { CustomValidation.message(ValidationReportError.Request.UnknownHeader.key, "Unknown request headers ${unknownHeaderNames.joinToString(separator = ",", prefix = "[", postfix = "]")}") }) else ValidationReport.empty()
    }

    private fun Collection<String>.notDeclaredIn(knownQueryParams: Set<Parameter>): Set<String> {

        val knownQueryParamNames = knownQueryParams.map { it.name.lowercase() }.toSet()
        return filterNot { paramName -> paramName.lowercase() in knownQueryParamNames }.toSet()
    }
}