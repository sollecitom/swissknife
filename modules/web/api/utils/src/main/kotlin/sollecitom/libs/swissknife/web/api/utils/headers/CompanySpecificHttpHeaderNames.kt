package sollecitom.libs.swissknife.web.api.utils.headers

internal class CompanySpecificHttpHeaderNames(private val companyName: String) : HttpHeaderNames {

    override val correlation: HttpHeaderNames.Correlation = CorrelationHeaderNames(companyName)

    override val gateway: HttpHeaderNames.Gateway = GatewayHeaderNames(companyName)

    private class CorrelationHeaderNames(private val companyName: String) : HttpHeaderNames.Correlation {

        override val invocationContext = "x-$companyName-invocation-context"
    }

    private class GatewayHeaderNames(private val companyName: String) : HttpHeaderNames.Gateway {

        override val externalInvocationId = "x-$companyName-external-trace-invocation-id"
        override val externalActionId = "x-$companyName-external-trace-action-id"
        override val specifiedLocale = "x-$companyName-specified-locale-language-tag"
        override val specifiedTargetTenant = "x-$companyName-specified-target-tenant"
        override val specifiedTargetCustomerId = "x-$companyName-specified-target-customer-id"
        override val isTest = "x-$companyName-is-test"
        override val toggles = "x-$companyName-toggles"
    }
}