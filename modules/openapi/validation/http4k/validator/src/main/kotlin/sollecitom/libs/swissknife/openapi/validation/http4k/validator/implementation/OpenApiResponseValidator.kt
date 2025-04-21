package sollecitom.libs.swissknife.openapi.validation.http4k.validator.implementation

import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport

internal interface OpenApiResponseValidator {

    fun validateResponse(path: String, method: Request.Method, response: Response): ValidationReport

    companion object
}