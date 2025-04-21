package sollecitom.libs.swissknife.kotlin.extensions.collections

fun <ELEMENT> List<ELEMENT>.circularSequence(): Sequence<ELEMENT> {

    var index = 0
    return sequence {
        while (true) {
            if (index == size) {
                index = 0
            }
            yield(get(index))
            index++
        }
    }
}

fun <ELEMENT> List<ELEMENT>.circularIterator(): Iterator<ELEMENT> = circularSequence().iterator()