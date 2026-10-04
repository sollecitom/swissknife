package sollecitom.libs.swissknife.test.utils.standard.output

import java.io.ByteArrayOutputStream
import java.io.PrintStream

inline fun <T> withCapturedStandardOutput(standardPrintStream: StandardPrintStream = StandardPrintStream.OUT, action: () -> T): Pair<T, List<String>> {

    val initialPrintStream = standardPrintStream.get()
    try {
        val outputStreamCaptor = ByteArrayOutputStream()
        val printStream = PrintStream(outputStreamCaptor)
        standardPrintStream.set(printStream)
        return printStream.use {
            val result = action()
            printStream.flush()
            outputStreamCaptor.flush()
            result to outputStreamCaptor.let { outputStreamCaptor.toString().lines().filterNot(String::isBlank) }
        }
    } finally {
        standardPrintStream.set(initialPrintStream)
    }
}
