package sollecitom.libs.swissknife.service.readiness.http4k

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import org.http4k.k8s.health.Completed
import org.http4k.k8s.health.Failed
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.readiness.domain.ReadinessAware
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import org.http4k.k8s.health.ReadinessCheck as Http4kReadinessCheck
import org.http4k.k8s.health.ReadinessCheckResult as Http4KReadinessCheckResult

fun ReadinessAware.http4kReadinessCheckWithModuleName(moduleName: Name, timeout: Duration = defaultReadinessTimeout): Http4kReadinessCheck = Http4kReadinessCheckAdapter(adapter = this, moduleName = moduleName, timeout = timeout)

val ReadinessAware.http4kReadinessCheck: Http4kReadinessCheck get() = Http4kReadinessCheckAdapter(adapter = this)

val defaultReadinessTimeout: Duration = 5.seconds

private class Http4kReadinessCheckAdapter(private val adapter: ReadinessAware, moduleName: Name? = null, private val timeout: Duration = defaultReadinessTimeout) : Http4kReadinessCheck {

    override val name: String = "${moduleName?.let { "${it.value} -> " } ?: ""}${adapter.readinessCheckName.value}"

    override fun invoke(): Http4KReadinessCheckResult {
        val readinessCheckResult = runBlocking { withTimeoutOrNull(timeout) { adapter.readinessCheck(this) } } ?: return Failed(name, "readiness check timed out after $timeout")
        return readinessCheckResult.asHttp4kReadinessResult(name)
    }
}

private fun ReadinessCheckResult.asHttp4kReadinessResult(name: String): Http4KReadinessCheckResult = when (this) {
    is ReadinessCheckResult.Passed -> Completed(name)
    is ReadinessCheckResult.Failed -> Failed(name, reason)
}