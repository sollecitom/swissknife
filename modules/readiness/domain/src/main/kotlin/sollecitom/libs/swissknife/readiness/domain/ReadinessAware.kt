package sollecitom.libs.swissknife.readiness.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import kotlinx.coroutines.CoroutineScope

/** A component that can report whether it is ready to serve traffic. */
interface ReadinessAware {

    val readinessCheckName: Name

    val readinessCheck: suspend CoroutineScope.() -> ReadinessCheckResult
}

context(scope: CoroutineScope)
suspend fun ReadinessAware.checkReadiness(): ReadinessCheckResult = readinessCheck.invoke(scope)

/** A component that requires an established connection before it can operate. */
interface RequiresConnection {

    suspend fun awaitConnection()
}