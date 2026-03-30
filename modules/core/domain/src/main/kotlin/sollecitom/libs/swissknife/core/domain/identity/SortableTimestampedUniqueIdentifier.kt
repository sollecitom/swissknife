package sollecitom.libs.swissknife.core.domain.identity

import sollecitom.libs.swissknife.core.domain.traits.Timestamped

/** An [Id] that is sortable, carries a creation timestamp, and is guaranteed to be unique (e.g. ULID, KSUID). */
sealed interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id, Comparable<SELF>, Timestamped