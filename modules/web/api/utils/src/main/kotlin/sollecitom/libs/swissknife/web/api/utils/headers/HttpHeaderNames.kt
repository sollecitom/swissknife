package sollecitom.libs.swissknife.web.api.utils.headers

interface HttpHeaderNames {

    val correlation: Correlation
    val gateway: Gateway

    interface Correlation {

        val invocationContext: String
    }

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

fun HttpHeaderNames.Companion.of(companyName: String): HttpHeaderNames = CompanySpecificHttpHeaderNames(companyName = companyName)