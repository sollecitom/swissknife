package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory

interface UuidVariantSelector {

    val v2: UniqueIdentifierFactory<UUID> get() = random
    val random: UniqueIdentifierFactory<UUID>
}