package sollecitom.libs.swissknife.logger.core.utils

import java.io.PrintStream

fun PrintStream.log(text: String) = synchronized(this) {
    println(text)
    flush()
}