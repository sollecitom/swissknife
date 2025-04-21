package sollecitom.libs.swissknife.core.domain.position

@JvmInline
value class Index(val value: Int) : Comparable<Index> {

    init {
        require(value >= 0) { "value cannot be negative" }
    }

    operator fun inc(): Index = Index(value + 1)

    operator fun plus(offset: Int): Index = Index(value + offset)
    operator fun minus(offset: Int): Index = Index(value - offset)

    override fun compareTo(other: Index) = value.compareTo(other.value)

    companion object {

        val ZERO get() = Index(0)
    }
}