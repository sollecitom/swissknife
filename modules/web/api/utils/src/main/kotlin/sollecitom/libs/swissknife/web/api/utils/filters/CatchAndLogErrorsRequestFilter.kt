package sollecitom.libs.swissknife.web.api.utils.filters

import sollecitom.libs.swissknife.http4k.utils.contentType
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.filter.ServerFilters

private object CatchAndLogErrorsRequestFilter : Loggable() {

    operator fun invoke(): Filter = Filter { next ->
        {
            try {
                next(it)
            } catch (error: Throwable) {
                if (error !is Exception) throw error
                logger.error(error) { "An error was caught while processing request ${it.toMessage()}" }
                Response(Status.INTERNAL_SERVER_ERROR).contentType(ContentType.TEXT_PLAIN).body("Something went wrong on the server.")
            }
        }
    }
}

val ServerFilters.catchAndLogErrors: Filter get() = CatchAndLogErrorsRequestFilter()