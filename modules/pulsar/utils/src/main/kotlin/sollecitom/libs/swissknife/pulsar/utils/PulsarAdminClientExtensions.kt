package sollecitom.libs.swissknife.pulsar.utils

import org.apache.avro.Schema
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.admin.PulsarAdminException
import org.apache.pulsar.client.api.schema.GenericRecord
import org.apache.pulsar.client.api.schema.GenericSchema
import org.apache.pulsar.common.policies.data.AutoTopicCreationOverride
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy
import org.apache.pulsar.common.policies.data.TenantInfo
import org.apache.pulsar.common.policies.data.TopicType
import org.apache.pulsar.common.schema.SchemaInfo
import org.apache.pulsar.common.schema.SchemaType
import org.apache.pulsar.client.api.Schema as PulsarSchema

fun PulsarAdmin.createTopic(fullyQualifiedTopic: String, numberOfPartitions: Int = 1) {

    require(numberOfPartitions >= 0) { "Number of partitions cannot be less than 0" }
    when (numberOfPartitions) {
        0 -> topics().createNonPartitionedTopic(fullyQualifiedTopic)
        else -> updateTopicPartitions(fullyQualifiedTopic, numberOfPartitions).onFailure { topics().createPartitionedTopic(fullyQualifiedTopic, numberOfPartitions) }
    }
}

private fun PulsarAdmin.updateTopicPartitions(fullyQualifiedTopic: String, numberOfPartitions: Int) = runCatching {

    val existingMetadata = topics().getPartitionedTopicMetadata(fullyQualifiedTopic)
    if (existingMetadata.partitions != numberOfPartitions) {
        topics().updatePartitionedTopic(fullyQualifiedTopic, numberOfPartitions)
    }
}

fun PulsarAdmin.createTenant(tenant: String) = tenants().createTenant(tenant, TenantInfo.builder().allowedClusters(clusters().clusters.toSet()).build())

fun PulsarAdmin.createNamespace(tenant: String, namespace: String) = namespaces().createNamespace("$tenant/$namespace")

fun PulsarAdmin.configureNamespace(tenant: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    val tenantNamespace = "$tenant/$namespace"
    namespaces().setAutoTopicCreation(tenantNamespace, AutoTopicCreationOverride.builder().allowAutoTopicCreation(allowTopicCreation).topicType(TopicType.PARTITIONED.name).build())
    namespaces().setAutoTopicCreation(tenantNamespace, AutoTopicCreationOverride.builder().allowAutoTopicCreation(allowTopicCreation).topicType(TopicType.NON_PARTITIONED.name).build())
    namespaces().setIsAllowAutoUpdateSchema(tenantNamespace, isAllowAutoUpdateSchema)
    namespaces().setSchemaValidationEnforced(tenantNamespace, schemaValidationEnforced)
    namespaces().setSchemaCompatibilityStrategy(tenantNamespace, schemaCompatibilityStrategy)
}

fun PulsarAdmin.createTenantAndNamespace(tenantId: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    createTenant(tenantId)
    createNamespace(tenantId, namespace)
    configureNamespace(tenantId, namespace, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy)
}

fun PulsarAdmin.ensureTenantAndNamespaceExist(tenant: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    ensureTenantExists(tenant)
    val newNamespaceWasCreated = ensureNamespaceExists(tenant, namespace)
    if (newNamespaceWasCreated) {
        configureNamespace(tenant, namespace, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy)
    }
}

fun PulsarAdmin.ensureTenantExists(tenantId: String): Boolean = try {
    createTenant(tenantId)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}

fun PulsarAdmin.ensureNamespaceExists(tenantId: String, namespace: String): Boolean = try {
    createNamespace(tenantId, namespace)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}

fun PulsarAdmin.ensureTopicExists(fullyQualifiedTopic: String, partitionsCount: Int = 1): Boolean = try {
    createTopic(fullyQualifiedTopic, partitionsCount)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}

fun PulsarAdmin.registerSchema(fullyQualifiedTopic: String, schema: Schema) = register(fullyQualifiedTopic, schema)

fun PulsarAdmin.registerSchema(fullyQualifiedTopic: String, schema: PulsarSchema<*>) = register(fullyQualifiedTopic, schema)

fun PulsarAdmin.ensureSchemaOnTopic(schema: Schema, fullyQualifiedTopic: String, partitionsCount: Int = 1) {

    ensureTopicExists(fullyQualifiedTopic, partitionsCount)
    registerSchema(fullyQualifiedTopic, schema)
}

fun PulsarAdmin.ensureSchemaOnTopic(schema: PulsarSchema<*>, fullyQualifiedTopic: String, partitionsCount: Int = 1) {

    ensureTopicExists(fullyQualifiedTopic, partitionsCount)
    registerSchema(fullyQualifiedTopic, schema)
}

private fun PulsarAdmin.register(topic: String, schema: Schema) = register(topic, schema.toPulsarGenericSchema())

private fun PulsarAdmin.register(topic: String, schema: PulsarSchema<*>) = try {
    schemas().createSchema(topic, schema.schemaInfo)
} catch (e: PulsarAdminException) {
    when (e.statusCode) {
        incompatibleSchemaErrorCode -> throw PulsarIncompatibleSchemaChangeException(e)
        else -> throw e
    }
}

private fun Schema.toPulsarGenericSchema(): GenericSchema<GenericRecord> = PulsarSchema.generic(SchemaInfo.builder().name(fullName).type(SchemaType.AVRO).schema(toString().toByteArray()).build())