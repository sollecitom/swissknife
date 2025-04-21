package sollecitom.libs.swissknife.readiness.domain

sealed interface ReadinessCheckResult {

    val passed: Boolean

    data object Passed : ReadinessCheckResult {

        override val passed = true
    }

    data class Failed(val reason: String) : ReadinessCheckResult {

        override val passed = false
    }
}

inline fun ReadinessCheckResult.ifFailed(action: (ReadinessCheckResult.Failed) -> Unit) {
    if (this is ReadinessCheckResult.Failed) {
        action(this)
    }
}

