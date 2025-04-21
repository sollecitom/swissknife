package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.logger.core.loggable.Loggable

class ProtectedValueData<out VALUE : Any, out METADATA>(override val value: ByteArray, override val name: Name, override val owner: Id, override val metadata: METADATA) : ProtectedValue<VALUE, METADATA> {

    class Accessible<out VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any>(private val protected: ProtectedValue<VALUE, METADATA>, private val deserialize: (ByteArray) -> VALUE, private val unprotect: suspend (ProtectedValue<VALUE, METADATA>) -> ByteArray) : ProtectedValue.Accessible<VALUE, METADATA, ACCESS_CONTEXT>, ProtectedValue<VALUE, METADATA> by protected {

        override suspend fun access(context: ACCESS_CONTEXT): VALUE {
            logger.info { "Accessed protected value with name '${name.value}' and owner with ID '${owner.stringValue}'" }
            val decoded = unprotect(this)
            return deserialize(decoded)
        }

        override fun toString(): String = "ProtectedValueData.Accessible(value=${value.contentToString()}, name=$name, owner=$owner, metadata=$metadata)"
    }

    private companion object : Loggable()

    override fun toString(): String = "ProtectedValueData(value=${value.contentToString()}, name=${name.value}, owner=${owner.stringValue}, metadata=$metadata)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProtectedValueData<*, *>

        if (!value.contentEquals(other.value)) return false
        if (name != other.name) return false
        if (owner != other.owner) return false
        if (metadata != other.metadata) return false
        return true
    }

    override fun hashCode(): Int {
        var result = value.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + owner.hashCode()
        result = 31 * result + (metadata?.hashCode() ?: 0)
        return result
    }
}

fun <VALUE : Any, METADATA, ACCESS_CONTEXT : Any> ProtectedValue<VALUE, METADATA>.accessible(deserialize: (ByteArray) -> VALUE, unprotect: suspend (ProtectedValue<VALUE, METADATA>) -> ByteArray): ProtectedValue.Accessible<VALUE, METADATA, ACCESS_CONTEXT> = ProtectedValueData.Accessible(this, deserialize, unprotect)