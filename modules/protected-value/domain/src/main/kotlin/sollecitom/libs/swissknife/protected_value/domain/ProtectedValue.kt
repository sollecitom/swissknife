package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

/** A value that is stored in protected (encrypted) form with associated metadata. Use [Accessible] to decrypt on demand. */
interface ProtectedValue<out VALUE : Any, out METADATA> {

    val value: ByteArray
    val name: Name
    val owner: Id
    val metadata: METADATA

    /** A [ProtectedValue] that can be decrypted given an access context. */
    interface Accessible<out VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any> : ProtectedValue<VALUE, METADATA> {

        suspend fun access(context: ACCESS_CONTEXT): VALUE
    }

    companion object
}