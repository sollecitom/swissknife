package sollecitom.libs.swissknife.messaging.domain.event.processing

sealed interface EventProcessingResult {

    data object Success : EventProcessingResult

    data object NoOp : EventProcessingResult
}