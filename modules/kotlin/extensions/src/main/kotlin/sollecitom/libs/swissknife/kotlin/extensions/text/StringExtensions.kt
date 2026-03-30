package sollecitom.libs.swissknife.kotlin.extensions.text

import java.util.*
import java.util.regex.Pattern

private val whitespace by lazy { Pattern.compile("\\s") }

fun String.withoutWhitespace(): String = whitespace.matcher(this).replaceAll("")

/** Replaces everything from the first occurrence of [delimiter] to the end with [replacement]. */
fun String.replaceFrom(delimiter: String, replacement: String, missingDelimiterValue: String = this): String {

    val index = indexOf(delimiter)
    return if (index == -1) missingDelimiterValue else replaceRange(index, length, replacement)
}

/** Replaces everything from the last occurrence of [delimiter] to the end with [replacement]. */
fun String.replaceFromLast(delimiter: String, replacement: String, missingDelimiterValue: String = this): String {

    val index = lastIndexOf(delimiter)
    return if (index == -1) missingDelimiterValue else replaceRange(index, length, replacement)
}

/** Removes everything from the first occurrence of [delimiter] to the end. */
fun String.removeFrom(delimiter: String): String {

    val index = indexOf(delimiter)
    return if (index == -1) this else removeRange(index, length)
}

/** Removes everything from the last occurrence of [delimiter] to the end. */
fun String.removeFromLast(delimiter: String): String {

    val index = lastIndexOf(delimiter)
    return if (index == -1) this else removeRange(index, length)
}

fun String.capitalized(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.indexOfOrNull(subString: String): Int? = indexOf(subString).takeUnless { it == -1 }

fun String.indexOfOrNull(char: Char): Int? = indexOf(char).takeUnless { it == -1 }

fun String.withoutDuplicatedWhitespace(): String = replace(Regex("\\s+"), " ")