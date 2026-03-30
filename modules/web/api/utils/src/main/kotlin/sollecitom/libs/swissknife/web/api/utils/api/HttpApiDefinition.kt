package sollecitom.libs.swissknife.web.api.utils.api

import org.http4k.core.Request
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames

/** Defines the conventions for an HTTP API, including header names. */
interface HttpApiDefinition {

    val headerNames: HttpHeaderNames

    companion object
}

/** Attaches the serialized [InvocationContext] as a correlation header on this request. */
context(api: HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>, serializeContext: (InvocationContext<*>) -> String): Request = header(api.headerNames.correlation.invocationContext, serializeContext(context))

context(_: HttpApiDefinition, context: InvocationContext<*>)
fun Request.withInvocationContext(serializeContext: (InvocationContext<*>) -> String): Request = withInvocationContext(context, serializeContext)