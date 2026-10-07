package sollecitom.libs.swissknife.core.domain.identity.factory.uuid

import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory
import kotlin.random.Random
import kotlin.uuid.Uuid

internal class UuidFactoryAdapter(private val random: Random) : UniqueIdentifierFactory<UUID> {

    override fun invoke(): UUID {
        val bytes = random.nextBytes(16)
        bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x40).toByte()
        bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte()
        return UUID(delegate = Uuid.fromByteArray(bytes))
    }

    override fun invoke(value: String): UUID = UUID(value)
}
