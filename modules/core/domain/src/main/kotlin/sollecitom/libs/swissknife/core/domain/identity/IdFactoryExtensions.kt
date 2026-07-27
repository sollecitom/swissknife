package sollecitom.libs.swissknife.core.domain.identity

/** Parses a string into the most specific [Id] type possible, trying ULID, KSUID, UUIDv7, UUID, then falling back to [StringId]. */
fun Id.Companion.fromString(stringValue: String): Id {

    runCatching { ULID(stringValue) }.getOrNull()?.let { return it }
    runCatching { KSUID(stringValue) }.getOrNull()?.let { return it }
    UUIDv7.parseOrNull(stringValue)?.let { return it }
    UUID.parseOrNull(stringValue)?.let { return it }
    return StringId(stringValue)
}