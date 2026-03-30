package sollecitom.libs.swissknife.kotlin.extensions.collections

/** Returns the power set (set of all subsets) of this collection. */
fun <ELEMENT> Collection<ELEMENT>.powerSet(): Set<Set<ELEMENT>> = powerSet(this, setOf(emptySet()))

private tailrec fun <ELEMENT> powerSet(left: Collection<ELEMENT>, accumulated: Set<Set<ELEMENT>>): Set<Set<ELEMENT>> = when {
    left.isEmpty() -> accumulated
    else -> powerSet(left.drop(1), accumulated + accumulated.map { it + left.first() })
}