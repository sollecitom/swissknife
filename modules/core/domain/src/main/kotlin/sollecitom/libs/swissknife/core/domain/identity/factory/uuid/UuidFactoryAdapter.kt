package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory
import kotlin.uuid.Uuid

internal object UuidFactoryAdapter : UniqueIdentifierFactory<UUID> {

    override fun invoke(): UUID = UUID(delegate = Uuid.generateV4())

    override fun invoke(value: String): UUID = UUID(value)
}
