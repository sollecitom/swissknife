package sollecitom.libs.swissknife.core.domain.versioning

/** A self-referential comparable version, used for entity versioning (e.g. optimistic concurrency). */
interface Version<SELF : Version<SELF>> : Comparable<SELF>