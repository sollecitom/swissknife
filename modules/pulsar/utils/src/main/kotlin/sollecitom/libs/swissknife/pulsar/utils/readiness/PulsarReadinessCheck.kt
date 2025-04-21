package sollecitom.libs.swissknife.pulsar.utils.readiness

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.readiness.domain.ReadinessAware
import sollecitom.libs.swissknife.readiness.domain.ReadinessCheckResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.PulsarClientException

class PulsarReadinessCheck(private val pulsar: PulsarClient, private val adapterName: Name = defaultAdapterName) : ReadinessAware {

    override val readinessCheckName get() = adapterName
    override val readinessCheck: suspend CoroutineScope.() -> ReadinessCheckResult get() = { tryToConnectToPulsar() }

    private suspend fun CoroutineScope.tryToConnectToPulsar(): ReadinessCheckResult {

        logger.debug { "Attempting to connect to '${adapterName.value}' as part of a readiness check." }
        return try {
            queryPartitionsForANonexistentTopic()
            logger.debug { "Successfully connected to '${adapterName.value}' as part of a readiness check." }
            ReadinessCheckResult.Passed
        } catch (error: Exception) {
            logger.debug(error) { "Failed to connect to ${readinessCheckName.value}" }
            ReadinessCheckResult.Failed("Failed to connect to '${adapterName.value}' as part of a readiness check.")
        }
    }

    private suspend fun queryPartitionsForANonexistentTopic() {

        try {
            pulsar.getPartitionsForTopic(NON_EXISTENT_TOPIC, false).await()
        } catch (_: PulsarClientException.NotFoundException) {
            // This is fine
        } catch (_: PulsarClientException.TopicDoesNotExistException) {
            // This is fine
        }
    }

    companion object : Loggable() {
        private const val NON_EXISTENT_TOPIC = "persistent://non-existent/non-existent/non-existent"
        val defaultAdapterName = "Apache Pulsar".let(::Name)
    }
}