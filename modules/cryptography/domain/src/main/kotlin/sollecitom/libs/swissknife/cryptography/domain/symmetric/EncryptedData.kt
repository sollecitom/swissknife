package sollecitom.libs.swissknife.cryptography.domain.symmetric

data class EncryptedData<METADATA : EncryptionMode.Metadata>(val content: ByteArray, val metadata: METADATA) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedData<*>

        if (!content.contentEquals(other.content)) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }

    override fun toString() = "EncryptedData(content=${content.contentToString()}, metadata=$metadata)"
}