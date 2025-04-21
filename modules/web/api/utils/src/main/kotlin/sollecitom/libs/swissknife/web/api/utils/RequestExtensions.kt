package sollecitom.libs.swissknife.web.api.utils

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import org.http4k.core.Request

fun Request.withInvocationContext(headerName: String, context: InvocationContext<*>, convert: (InvocationContext<*>) -> String): Request = header(headerName, convert(context))