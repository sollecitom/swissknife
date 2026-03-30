package sollecitom.libs.swissknife.web.api.utils.headers

/** Defines the custom HTTP header names used by the API for correlation and gateway metadata. */
interface HttpHeaderNames {

    val correlation: Correlation
    val gateway: Gateway

    /** Header names for request correlation and tracing. */
    interface Correlation {

        val invocationContext: String
    }

    /** Header names for gateway-injected metadata (tenant, locale, test mode, etc.). */
    interface Gateway {

        val externalInvocationId: String
        val externalActionId: String
        val specifiedLocale: String
        val specifiedTargetTenant: String
        val specifiedTargetCustomerId: String
        val isTest: String
        val toggles: String
    }

    companion object
}

/** Creates [HttpHeaderNames] with headers prefixed by the given [companyName] (e.g., `x-{companyName}-invocation-context`). */
fun HttpHeaderNames.Companion.of(companyName: String): HttpHeaderNames = CompanySpecificHttpHeaderNames(companyName = companyName)