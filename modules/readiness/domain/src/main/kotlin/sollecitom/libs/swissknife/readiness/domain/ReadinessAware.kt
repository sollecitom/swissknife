package sollecitom.libs.swissknife.readiness.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import kotlinx.coroutines.CoroutineScope

interface ReadinessAware {

    val readinessCheckName: Name

    val readinessCheck: suspend CoroutineScope.() -> ReadinessCheckResult
}

context(scope: CoroutineScope)
suspend fun ReadinessAware.checkReadiness(): ReadinessCheckResult = readinessCheck.invoke(scope)

interface RequiresConnection {

    suspend fun awaitConnection()
}