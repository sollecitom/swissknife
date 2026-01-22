package sollecitom.libs.swissknife.hashing.utils

@JvmInline
value class HashBinaryResult internal constructor(val bytes: ByteArray) {

    companion object {

        fun create(bytes: ByteArray): HashBinaryResult {
            return HashBinaryResult(bytes)
        }
    }
}