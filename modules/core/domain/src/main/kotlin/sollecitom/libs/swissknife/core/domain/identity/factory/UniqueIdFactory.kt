package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.SortableTimestampedUniqueIdentifier
import sollecitom.libs.swissknife.core.domain.identity.factory.ksuid.KsuidVariantSelector
import sollecitom.libs.swissknife.core.domain.identity.factory.ulid.UlidVariantSelector
import sollecitom.libs.swissknife.core.domain.identity.factory.uuid.UuidVariantSelector

interface UniqueIdFactory {

    val ulid: UlidVariantSelector
    val ksuid: KsuidVariantSelector
    val uuid: UuidVariantSelector
    val internal: SortableTimestampedUniqueIdentifierFactory<*>
    val external: UniqueIdentifierFactory<Id>

    companion object
}

operator fun UniqueIdFactory.invoke(): SortableTimestampedUniqueIdentifier<*> = internal()

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory