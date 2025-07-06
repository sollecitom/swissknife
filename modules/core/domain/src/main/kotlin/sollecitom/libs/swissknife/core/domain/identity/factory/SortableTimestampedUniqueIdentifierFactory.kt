package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.SortableTimestampedUniqueIdentifier
import kotlin.time.Instant
import kotlin.time.toKotlinInstant
import java.time.Instant as JavaInstant

interface SortableTimestampedUniqueIdentifierFactory<ID : SortableTimestampedUniqueIdentifier<ID>> {

    operator fun invoke(): ID

    operator fun invoke(timestamp: Instant): ID
}

operator fun <ID : SortableTimestampedUniqueIdentifier<ID>> SortableTimestampedUniqueIdentifierFactory<ID>.invoke(instant: JavaInstant) = invoke(instant.toKotlinInstant())