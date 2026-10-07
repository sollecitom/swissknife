package sollecitom.libs.swissknife.core.domain.networking

/** A validated non-privileged port number (1024-65535). Despite the name, this is wider than the IANA dynamic/ephemeral range (49152-65535). */
@JvmInline
value class EphemeralPort(val value: Int) : Comparable<EphemeralPort> {

    init {
        require(value in range) { "value must be within range $range" }
    }

    override fun compareTo(other: EphemeralPort) = value.compareTo(other.value)

    companion object {

        val range get() = Port.ephemeralRange
    }
}