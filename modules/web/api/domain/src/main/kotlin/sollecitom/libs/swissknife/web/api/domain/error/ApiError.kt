package sollecitom.libs.swissknife.web.api.domain.error

data class ApiError(val message: String, val code: String) {

    constructor(code: ErrorCode) : this(code.message, code.value)

    companion object
}