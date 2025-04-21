package sollecitom.libs.swissknife.serialization.domain

interface Deserializer<in SERIALIZED : Any, out VALUE : Any> {

    fun deserialize(value: SERIALIZED): VALUE
}