package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

/** A value that is stored in protected (encrypted) form with associated metadata. Use [Accessible] to decrypt on demand. */
interface ProtectedValue<out VALUE : Any, out METADATA> {

    val value: ByteArray
    val name: Name
    val owner: Id
    val metadata: METADATA

    /** A [ProtectedValue] that can be decrypted given an access context. Every access first goes through the factory's [AccessHook]. */
    interface Accessible<out VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any> : ProtectedValue<VALUE, METADATA> {

        suspend fun access(context: ACCESS_CONTEXT): VALUE
    }

    /** Called before a protected value is decrypted: records who accessed what and when, and refuses access by throwing. */
    fun interface AccessHook<in ACCESS_CONTEXT : Any, in METADATA> {

        suspend fun beforeAccess(context: ACCESS_CONTEXT, value: ProtectedValue<*, METADATA>)
    }

    companion object
}