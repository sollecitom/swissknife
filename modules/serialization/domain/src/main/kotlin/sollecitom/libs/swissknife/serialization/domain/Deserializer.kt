package sollecitom.libs.swissknife.serialization.domain

/** Converts a serialized form [SERIALIZED] back into a value of type [VALUE]. */
interface Deserializer<in SERIALIZED : Any, out VALUE : Any> {

    fun deserialize(value: SERIALIZED): VALUE
}