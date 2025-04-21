package sollecitom.libs.swissknife.messaging.domain.event.processing

sealed interface EventProcessingResult {

    data object Success : EventProcessingResult

    data class Failure(val error: Throwable, val message: String? = error.message) : EventProcessingResult

    data object NoOp : EventProcessingResult
}

fun Throwable.asProcessingFailure() = let(EventProcessingResult::Failure)