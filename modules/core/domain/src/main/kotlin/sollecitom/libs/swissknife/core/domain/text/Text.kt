package sollecitom.libs.swissknife.core.domain.text

@JvmInline
value class Text(val value: String) {

    init {
        require(value.isNotBlank()) { "value cannot be blank" }
    }

    val length: Int get() = value.length

    companion object
}