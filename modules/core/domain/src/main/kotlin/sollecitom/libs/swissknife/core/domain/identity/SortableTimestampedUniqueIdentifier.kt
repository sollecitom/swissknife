package sollecitom.libs.swissknife.core.domain.identity

import sollecitom.libs.swissknife.core.domain.traits.Timestamped

sealed interface SortableTimestampedUniqueIdentifier<SELF : SortableTimestampedUniqueIdentifier<SELF>> : Id, Comparable<SELF>, Timestamped