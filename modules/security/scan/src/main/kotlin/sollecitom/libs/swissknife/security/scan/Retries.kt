package sollecitom.libs.swissknife.security.scan

internal sealed interface Attempt<out T> {

    data class Succeeded<out T>(val value: T) : Attempt<T>

    data class Failed(val cause: Exception, val output: String) : Attempt<Nothing>
}

internal sealed interface RetryOutcome<out T> {

    data class Succeeded<out T>(val value: T) : RetryOutcome<T>

    data class GaveUp(val attempts: Int, val lastFailure: Attempt.Failed) : RetryOutcome<Nothing>
}

internal fun <T> attemptWithRetries(maximumAttempts: Int, isWorthRetrying: (Attempt.Failed) -> Boolean, beforeRetry: (Attempt.Failed, attemptsLeft: Int) -> Unit, attempt: () -> Attempt<T>): RetryOutcome<T> {

    var attempts = 0
    while (true) {
        attempts++
        when (val outcome = attempt()) {
            is Attempt.Succeeded -> return RetryOutcome.Succeeded(outcome.value)
            is Attempt.Failed -> {
                val attemptsLeft = maximumAttempts - attempts
                if (attemptsLeft == 0 || !isWorthRetrying(outcome)) return RetryOutcome.GaveUp(attempts, outcome)
                beforeRetry(outcome, attemptsLeft)
            }
        }
    }
}
