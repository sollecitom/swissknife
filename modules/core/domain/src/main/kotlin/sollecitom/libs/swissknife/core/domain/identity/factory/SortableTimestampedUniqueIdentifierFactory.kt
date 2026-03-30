package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.SortableTimestampedUniqueIdentifier
import kotlin.time.Instant
import kotlin.time.toKotlinInstant
import java.time.Instant as JavaInstant

/** Factory for generating sortable, timestamped unique identifiers. */
interface SortableTimestampedUniqueIdentifierFactory<ID : SortableTimestampedUniqueIdentifier<ID>> {

    /** Generates a new identifier using the current time. */
    operator fun invoke(): ID

    /** Generates a new identifier with the specified timestamp. */
    operator fun invoke(timestamp: Instant): ID
}

operator fun <ID : SortableTimestampedUniqueIdentifier<ID>> SortableTimestampedUniqueIdentifierFactory<ID>.invoke(instant: JavaInstant) = invoke(instant.toKotlinInstant())