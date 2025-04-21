package sollecitom.libs.swissknife.openapi.validation.request.validator

sealed class ValidationReportError(val key: String) {

    sealed class Request(key: String) : ValidationReportError(key) {

        data object MissingRequiredHeader : Request("validation.request.parameter.header.missing")

        data object UnknownHeader : Request("validation.request.parameter.header.unknown")

        data object UnknownPath : Request("validation.request.path.missing")

        data object ContentTypeNotAllowed : Request("validation.request.contentType.notAllowed")

        data object InvalidJson : Request("validation.request.body.schema.invalidJson")

        sealed class Body(key: String) : Request(key) {

            data object MissingRequiredField : Body("validation.request.body.schema.required")

            data object InvalidType : Body("validation.request.body.schema.type")
        }
    }

    sealed class Response(key: String) : ValidationReportError(key) {

        data object MissingRequiredHeader : Request("validation.response.header.missing")

        data object UnknownHeader : Response("validation.response.header.unknown")

        sealed class Body(key: String) : Response(key) {

            data object MissingRequiredField : Body("validation.response.body.schema.required")

            data object InvalidType : Body("validation.response.body.schema.type")
        }
    }
}