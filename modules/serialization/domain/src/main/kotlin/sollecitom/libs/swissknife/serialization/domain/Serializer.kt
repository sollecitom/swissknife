package sollecitom.libs.swissknife.serialization.domain

/** Converts a value of type [VALUE] into its serialized form [SERIALIZED]. */
interface Serializer<in VALUE : Any, out SERIALIZED : Any> {

    fun serialize(value: VALUE): SERIALIZED
}

