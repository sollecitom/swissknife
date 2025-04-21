package sollecitom.libs.swissknife.pulsar.utils

import org.apache.avro.Schema
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

fun PulsarAdmin.ensureTenantAndNamespaceExistForTopic(topic: Topic, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    ensureTenantAndNamespaceExist(tenant = topic.namespace!!.tenant.value, namespace = topic.namespace!!.name.value, allowTopicCreation = allowTopicCreation, isAllowAutoUpdateSchema = isAllowAutoUpdateSchema, schemaValidationEnforced = schemaValidationEnforced, schemaCompatibilityStrategy = schemaCompatibilityStrategy)
}

fun PulsarAdmin.ensureTopicExists(topic: Topic, partitionsCount: Int = 1) = ensureTopicExists(fullyQualifiedTopic = topic.fullName.value, partitionsCount = partitionsCount)

fun PulsarAdmin.ensureTopicExistsWithSchema(topic: Topic, schema: Schema, partitionsCount: Int = 1, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    ensureTenantAndNamespaceExistForTopic(topic = topic, allowTopicCreation = allowTopicCreation, isAllowAutoUpdateSchema = isAllowAutoUpdateSchema, schemaValidationEnforced = schemaValidationEnforced, schemaCompatibilityStrategy = schemaCompatibilityStrategy)
    ensureTopicExists(topic = topic, partitionsCount = partitionsCount)
    registerSchema(fullyQualifiedTopic = topic.fullName.value, schema = schema)
}

