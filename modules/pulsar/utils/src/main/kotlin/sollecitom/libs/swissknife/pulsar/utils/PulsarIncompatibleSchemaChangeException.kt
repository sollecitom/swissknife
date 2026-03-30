package sollecitom.libs.swissknife.pulsar.utils

import org.apache.pulsar.client.admin.PulsarAdminException
import org.apache.pulsar.shade.javax.ws.rs.ClientErrorException

/** Thrown when a schema registration is rejected by Pulsar due to a compatibility violation. */
class PulsarIncompatibleSchemaChangeException(cause: PulsarAdminException) : ClientErrorException(cause.message, cause.statusCode)