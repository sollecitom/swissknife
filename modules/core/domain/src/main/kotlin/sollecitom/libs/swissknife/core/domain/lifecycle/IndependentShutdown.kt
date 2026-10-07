package sollecitom.libs.swissknife.core.domain.lifecycle

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlin.coroutines.cancellation.CancellationException

suspend fun stopReportingFailure(component: String, stop: suspend () -> Unit) {

    try {
        stop()
        IndependentShutdown.logger.info { "Stopped $component" }
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        IndependentShutdown.logger.error(error = error) { "Failed to stop $component" }
    }
}

fun closeReportingFailure(resource: String, close: () -> Unit) {

    runCatching(close).onFailure { error -> IndependentShutdown.logger.error(error = error) { "Failed to close the $resource" } }
}

private object IndependentShutdown : Loggable()
