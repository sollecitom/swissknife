package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

interface ProtectedValueFactory<in ACCESS_CONTEXT : Any, out METADATA> {

    suspend fun <VALUE : Any> protectValue(value: VALUE, valueName: Name, owner: Id, serialize: (VALUE) -> ByteArray, iv: ByteArray? = null): ProtectedValue<VALUE, METADATA>

    interface Accessible<in ACCESS_CONTEXT : Any, METADATA> : ProtectedValueFactory<ACCESS_CONTEXT, METADATA> {

        fun <VALUE : Any> makeAccessible(protectedValue: ProtectedValue<VALUE, METADATA>, deserialize: (ByteArray) -> VALUE): ProtectedValue.Accessible<VALUE, METADATA, ACCESS_CONTEXT>
    }

    interface Typed<VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any> {

        suspend fun protectValue(value: VALUE, valueName: Name, owner: Id): ProtectedValue<VALUE, METADATA>

        interface Accessible<VALUE : Any, METADATA, in ACCESS_CONTEXT : Any> : Typed<VALUE, METADATA, ACCESS_CONTEXT> {

            fun makeAccessible(protectedValue: ProtectedValue<VALUE, METADATA>): ProtectedValue.Accessible<VALUE, METADATA, ACCESS_CONTEXT>
        }
    }

    companion object
}