package sollecitom.libs.swissknife.openapi.validation.http4k.validator

import com.atlassian.oai.validator.report.ValidationReport
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

/** Validates http4k requests and responses against an OpenAPI specification. */
interface Http4kOpenApiValidator {

    /** Validates an incoming [request] against the OpenAPI spec, returning any violations. */
    fun validate(request: Request): ValidationReport

    /** Validates a [response] for the given [path] and [method] against the OpenAPI spec. */
    fun validate(path: String, method: Method, acceptHeader: ContentType, response: Response): ValidationReport

    companion object
}