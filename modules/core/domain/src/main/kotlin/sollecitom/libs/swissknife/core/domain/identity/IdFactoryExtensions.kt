package sollecitom.libs.swissknife.core.domain.identity

fun Id.Companion.fromString(stringValue: String): Id {

    return runCatching { ULID(stringValue) }.getOrElse { runCatching { KSUID(stringValue) }.getOrElse { runCatching { UUID(stringValue) }.getOrElse { StringId(stringValue) } } }
}