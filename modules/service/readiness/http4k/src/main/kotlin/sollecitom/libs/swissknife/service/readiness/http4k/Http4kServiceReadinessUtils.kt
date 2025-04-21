package sollecitom.libs.swissknife.service.readiness.http4k

import kotlinx.coroutines.runBlocking
import org.http4k.k8s.health.Completed
import org.http4k.k8s.health.Failed
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.readiness.domain.ReadinessAware
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import org.http4k.k8s.health.ReadinessCheck as Http4kReadinessCheck
import org.http4k.k8s.health.ReadinessCheckResult as Http4KReadinessCheckResult

fun ReadinessAware.http4kReadinessCheckWithModuleName(moduleName: Name): Http4kReadinessCheck = Http4kReadinessCheckAdapter(adapter = this, moduleName = moduleName)

val ReadinessAware.http4kReadinessCheck: Http4kReadinessCheck get() = Http4kReadinessCheckAdapter(adapter = this)

private class Http4kReadinessCheckAdapter(private val adapter: ReadinessAware, moduleName: Name? = null) : Http4kReadinessCheck {

    override val name: String = "${moduleName?.let { "${it.value} -> " } ?: ""}${adapter.readinessCheckName.value}"

    override fun invoke(): Http4KReadinessCheckResult {
        val readinessCheckResult = runBlocking { adapter.readinessCheck(this) }
        return readinessCheckResult.asHttp4kReadinessResult(name)
    }
}

private fun ReadinessCheckResult.asHttp4kReadinessResult(name: String): Http4KReadinessCheckResult = when (this) {
    is ReadinessCheckResult.Passed -> Completed(name)
    is ReadinessCheckResult.Failed -> Failed(name, reason)
}