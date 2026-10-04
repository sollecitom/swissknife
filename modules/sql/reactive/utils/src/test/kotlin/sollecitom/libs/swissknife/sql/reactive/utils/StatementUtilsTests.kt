package sollecitom.libs.swissknife.sql.reactive.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.r2dbc.spi.Result
import io.r2dbc.spi.Statement
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.reactivestreams.Publisher

@TestInstance(PER_CLASS)
class StatementUtilsTests {

    @Test
    fun `binding a null by index uses the boxed type`() {

        val statement = NullTypeRecordingStatement()

        statement.bindNullable<Int>(0, null)
        statement.bindNullable<Long>(1, null)
        statement.bindNullable<Boolean>(2, null)

        assertThat(statement.nullTypes).isEqualTo(listOf<Class<*>>(Integer::class.java, java.lang.Long::class.java, java.lang.Boolean::class.java))
    }

    @Test
    fun `binding a null by name uses the boxed type`() {

        val statement = NullTypeRecordingStatement()

        statement.bindNullable<Int>("a", null)
        statement.bindNullable<Long>("b", null)
        statement.bindNullable<Boolean>("c", null)

        assertThat(statement.nullTypes).isEqualTo(listOf<Class<*>>(Integer::class.java, java.lang.Long::class.java, java.lang.Boolean::class.java))
    }

    private class NullTypeRecordingStatement : Statement {

        val nullTypes = mutableListOf<Class<*>>()

        override fun add() = this
        override fun bind(index: Int, value: Any) = this
        override fun bind(name: String, value: Any) = this
        override fun bindNull(index: Int, type: Class<*>) = also { nullTypes += type }
        override fun bindNull(name: String, type: Class<*>) = also { nullTypes += type }
        override fun execute(): Publisher<out Result> = error("not executable")
    }
}
