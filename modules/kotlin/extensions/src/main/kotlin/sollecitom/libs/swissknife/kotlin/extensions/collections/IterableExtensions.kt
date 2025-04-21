package sollecitom.libs.swissknife.kotlin.extensions.collections

fun <ELEMENT> Iterable<ELEMENT>.filterIn(allowedElements: Set<ELEMENT>) = filter { it in allowedElements }