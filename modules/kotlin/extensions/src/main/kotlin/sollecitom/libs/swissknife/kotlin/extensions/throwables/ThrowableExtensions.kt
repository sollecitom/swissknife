package sollecitom.libs.swissknife.kotlin.extensions.throwables

/** Lazily traverses the causal chain, handling circular references. */
val Throwable.causes: Sequence<Throwable>
    get() = sequence {
        val seenCauses = mutableSetOf<Throwable>()
        var cause: Throwable? = cause
        while (cause != null && cause !in seenCauses) {
            seenCauses += cause
            yield(cause)
            cause = cause.cause
        }
    }