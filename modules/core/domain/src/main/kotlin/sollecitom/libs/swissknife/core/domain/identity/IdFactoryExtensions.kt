package sollecitom.libs.swissknife.core.domain.identity

/** Wraps [stringValue] in a [StringId], keeping the exact text. Use [fromTypedString] to round-trip the [Id] type. */
fun Id.Companion.fromString(stringValue: String): Id = StringId(stringValue)

/** Encodes this [Id] as `<type>:<value>` (e.g. `ulid:01ARZ3NDEKTSV4RRFFQ69G5FAV`), so that [fromTypedString] restores both type and value. */
fun Id.toTypedString(): String = "$typeName$TYPE_SEPARATOR$stringValue"

/** Parses the `<type>:<value>` form produced by [toTypedString]. */
fun Id.Companion.fromTypedString(typedValue: String): Id {

    val type = typedValue.substringBefore(TYPE_SEPARATOR, missingDelimiterValue = "")
    val value = typedValue.substringAfter(TYPE_SEPARATOR)
    require(type.isNotEmpty()) { "Expected a typed ID in the form '<type>:<value>', but got '$typedValue'" }
    return when (type) {
        ULID_TYPE -> ULID(value)
        KSUID_TYPE -> KSUID(value)
        UUID_V7_TYPE -> UUIDv7(value)
        UUID_TYPE -> UUID(value)
        STRING_TYPE -> StringId(value)
        else -> throw IllegalArgumentException("Unknown ID type '$type' in '$typedValue'")
    }
}

private val Id.typeName: String
    get() = when (this) {
        is ULID -> ULID_TYPE
        is KSUID -> KSUID_TYPE
        is UUIDv7 -> UUID_V7_TYPE
        is UUID -> UUID_TYPE
        is StringId -> STRING_TYPE
    }

private const val TYPE_SEPARATOR = ':'
private const val ULID_TYPE = "ulid"
private const val KSUID_TYPE = "ksuid"
private const val UUID_V7_TYPE = "uuidv7"
private const val UUID_TYPE = "uuid"
private const val STRING_TYPE = "string"
