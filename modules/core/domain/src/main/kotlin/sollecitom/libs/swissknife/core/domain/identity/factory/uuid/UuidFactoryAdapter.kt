package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory
import java.util.UUID.randomUUID as randomJavaUUID

internal object UuidFactoryAdapter : UniqueIdentifierFactory<UUID> {

    override fun invoke(): UUID = UUID(delegate = randomJavaUUID())

    override fun invoke(value: String): UUID = UUID(value)
}