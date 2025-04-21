package sollecitom.libs.swissknife.web.api.utils.filters.correlation

import org.http4k.lens.RequestKey
import org.http4k.lens.RequestLens
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext

object InvocationContextFilter {

    val key: Key by lazy { Key(mandatory = RequestKey.required("invocation.context"), optional = RequestKey.optional("invocation.context")) }

    data class Key(val mandatory: RequestLens<InvocationContext<Access>>, val optional: RequestLens<InvocationContext<Access>?>)
}