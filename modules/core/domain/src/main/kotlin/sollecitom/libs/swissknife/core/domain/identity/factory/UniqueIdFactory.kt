package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.SortableTimestampedUniqueIdentifier
import sollecitom.libs.swissknife.core.domain.identity.factory.ksuid.KsuidVariantSelector
import sollecitom.libs.swissknife.core.domain.identity.factory.ulid.UlidVariantSelector
import sollecitom.libs.swissknife.core.domain.identity.factory.uuid.UuidVariantSelector

/** Central factory providing access to multiple ID generation strategies (ULID, KSUID, UUID). */
interface UniqueIdFactory {

    val ulid: UlidVariantSelector
    val ksuid: KsuidVariantSelector
    val uuid: UuidVariantSelector
    /** Default factory for internal IDs (sortable, timestamped). */
    val internal: SortableTimestampedUniqueIdentifierFactory<*>
    /** Default factory for external-facing IDs. */
    val external: UniqueIdentifierFactory<Id>

    companion object
}

/** Shorthand to generate an internal sortable ID via [UniqueIdFactory.internal]. */
operator fun UniqueIdFactory.invoke(): SortableTimestampedUniqueIdentifier<*> = internal()

val Id.Companion.Factory: UniqueIdFactory.Companion get() = UniqueIdFactory