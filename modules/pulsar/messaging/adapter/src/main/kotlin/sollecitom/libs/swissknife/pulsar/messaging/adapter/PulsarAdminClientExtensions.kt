package sollecitom.libs.swissknife.pulsar.messaging.adapter

import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.utils.ensureTenantAndNamespaceExist
import sollecitom.libs.swissknife.pulsar.utils.ensureTopicExists
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy

fun PulsarAdmin.ensureTopicExists(topic: Topic, numberOfPartitions: Int = 1, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    topic.namespace?.let { ensureTenantAndNamespaceExist(it.tenant.value, it.name.value, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy) }
    ensureTopicExists(topic.fullName.value, numberOfPartitions)
}