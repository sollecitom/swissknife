package sollecitom.libs.swissknife.web.api.utils.api

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import org.http4k.core.Request

interface HttpApiDefinition {

    val headerNames: HttpHeaderNames

    companion object
}

context(HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>, serializeContext: (InvocationContext<*>) -> String): Request = header(headerNames.correlation.invocationContext, serializeContext(context))

context(HttpApiDefinition, InvocationContext<*>)
fun Request.withInvocationContext(serializeContext: (InvocationContext<*>) -> String): Request = withInvocationContext(this@InvocationContext, serializeContext)