package sollecitom.libs.swissknife.web.api.domain.error

sealed class ErrorCode(val value: String, val message: String) {

    data object AuthenticatedAccessRequired : ErrorCode(value = "01J15B4WXA9GZG7S7TG96DA6EC", message = "The invocation requires an authenticated access")

    data object UnauthenticatedAccessRequired : ErrorCode(value = "01J15B7BGJDEVQHWWY6ZJ7QMCA", message = "The invocation requires an unauthenticated access")
}