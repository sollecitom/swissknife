package sollecitom.libs.swissknife.test.utils.standard.output

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isSameInstanceAs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class StandardOutputUtilsTests {

    @Test
    fun `capturing the standard error stream`() {

        val (_, lines) = withCapturedStandardOutput(StandardPrintStream.ERR) { System.err.println("on error") }

        assertThat(lines).containsExactly("on error")
    }

    @Test
    fun `capturing the standard error stream leaves the standard output stream untouched`() {

        val standardOutput = System.out

        withCapturedStandardOutput(StandardPrintStream.ERR) { }

        assertThat(System.out).isSameInstanceAs(standardOutput)
    }

    @Test
    fun `capturing the standard output stream`() {

        val (_, lines) = withCapturedStandardOutput { println("on output") }

        assertThat(lines).containsExactly("on output")
    }
}
