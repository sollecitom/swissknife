package sollecitom.libs.swissknife.web.api.domain.endpoint

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler

/** An HTTP endpoint with a path, allowed methods, and an http4k route handler. */
interface Endpoint {

    val path: String
    val methods: Set<Method>
    val route: RoutingHttpHandler
}