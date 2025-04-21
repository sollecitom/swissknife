package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler

interface Endpoint {

    val path: String
    val methods: Set<Method>
    val route: RoutingHttpHandler
}