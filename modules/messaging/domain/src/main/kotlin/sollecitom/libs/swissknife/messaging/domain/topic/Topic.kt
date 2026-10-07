package sollecitom.libs.swissknife.messaging.domain.topic

import sollecitom.libs.swissknife.core.domain.text.Name
import java.util.regex.Pattern

/** A messaging topic with protocol (persistent/non-persistent), namespace, and name. Topics created without a namespace get [Namespace.default] (`public/default`), as in Pulsar. Use [Topic.parse] or factory methods to create. */
sealed class Topic(val persistent: Boolean, val namespace: Namespace, val name: Name) {

    val protocol: Name get() = if (persistent) Persistent.protocol else NonPersistent.protocol
    val fullName: Name = fullRawName(protocol, namespace, name)

    override fun toString() = fullName.value

    class Persistent(namespace: Namespace = Namespace.default, name: Name) : Topic(true, namespace, name) {

        companion object {
            val protocol = "persistent".let(::Name)
        }
    }

    class NonPersistent(namespace: Namespace = Namespace.default, name: Name) : Topic(false, namespace, name) {

        companion object {
            val protocol = "non-persistent".let(::Name)
        }
    }

    data class Namespace(val tenant: Name, val name: Name) {

        companion object {

            val default = Namespace(tenant = Name("public"), name = Name("default"))

            fun parse(namespace: String): Namespace = Topic.parse("$namespace/some-topic").namespace
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Topic) return false
        if (persistent != other.persistent) return false
        if (namespace != other.namespace) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = persistent.hashCode()
        result = 31 * result + namespace.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {

        internal const val SEPARATOR = "/"
        private const val EXPECTED_PARTS_COUNT = 5
        private const val PROTOCOL_GROUP = "(persistent|non-persistent)"
        private const val TENANT_GROUP = "([\\w.=:\\-]+)"
        private const val NAMESPACE_GROUP = "([\\w.=:\\-]+)"
        private const val NAME_GROUP = "([\\w.=:\\-]+)"
        private const val PATTERN = "$PROTOCOL_GROUP://$TENANT_GROUP/$NAMESPACE_GROUP/$NAME_GROUP"
        private val compiled by lazy { Pattern.compile(PATTERN) }

        fun parse(rawTopic: String): Topic {

            require(rawTopic.split(SEPARATOR).size <= EXPECTED_PARTS_COUNT) { "Invalid topic. Maximum $EXPECTED_PARTS_COUNT parts are expected." }
            val matcher = compiled.matcher(rawTopic)
            if (!matcher.matches()) {
                error("Topic format '$rawTopic' does not match the expected pattern $PATTERN")
            }
            val protocol = matcher.group(1)?.let(::Name) ?: Persistent.protocol
            val namespace = Namespace(tenant = matcher.group(2).let(::Name), name = matcher.group(3).let(::Name))
            val topicName = matcher.group(4).let(::Name)
            return of(protocol, namespace, topicName)
        }

        fun of(protocol: Name, namespace: Namespace = Namespace.default, name: Name): Topic = when (protocol) {
            Persistent.protocol -> of(true, namespace, name)
            NonPersistent.protocol -> of(false, namespace, name)
            else -> error("Unknown topic protocol ${protocol.value}")
        }

        fun of(persistent: Boolean, namespace: Namespace = Namespace.default, name: Name): Topic = when (persistent) {
            true -> persistent(name, namespace)
            false -> nonPersistent(name, namespace)
        }

        fun fullRawName(protocol: Name, namespace: Namespace, name: Name): Name = Name("${protocol.value}://${namespace.tenant.value}/${namespace.name.value}/${name.value}")

        fun persistent(name: Name, namespace: Namespace = Namespace.default): Topic = Persistent(namespace, name)

        fun nonPersistent(name: Name, namespace: Namespace = Namespace.default): Topic = NonPersistent(namespace, name)
    }

    @JvmInline
    value class Partition(val index: Int) {

        init {
            require(index >= 0)
        }
    }
}