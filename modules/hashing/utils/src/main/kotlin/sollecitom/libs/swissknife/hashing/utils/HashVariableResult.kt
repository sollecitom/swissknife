package sollecitom.libs.swissknife.hashing.utils

@JvmInline
value class HashVariableResult internal constructor(val bytes: ByteArray) {

    companion object {

        fun create(bytes: ByteArray): HashVariableResult {
            return HashVariableResult(bytes)
        }
    }
}