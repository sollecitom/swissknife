package sollecitom.libs.swissknife.core.domain.identity

/** Parses a string into the most specific [Id] type possible, trying ULID, KSUID, UUID, then falling back to [StringId]. */
fun Id.Companion.fromString(stringValue: String): Id {

    return runCatching { ULID(stringValue) }.getOrElse { runCatching { KSUID(stringValue) }.getOrElse { runCatching { UUID(stringValue) }.getOrElse { StringId(stringValue) } } }
}