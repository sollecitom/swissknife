package sollecitom.libs.swissknife.pulsar.utils

import org.apache.pulsar.client.admin.PulsarAdminException
import org.apache.pulsar.shade.javax.ws.rs.ClientErrorException

class PulsarIncompatibleSchemaChangeException(cause: PulsarAdminException) : ClientErrorException(cause.message, cause.statusCode)