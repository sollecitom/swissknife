package sollecitom.libs.swissknife.pulsar.utils

import org.apache.pulsar.client.admin.PulsarAdminException

/** Thrown when a schema registration is rejected by Pulsar due to a compatibility violation. */
class PulsarIncompatibleSchemaChangeException(override val cause: PulsarAdminException) : RuntimeException(cause.message, cause)
