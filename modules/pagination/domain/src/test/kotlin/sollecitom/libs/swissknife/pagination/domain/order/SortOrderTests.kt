package sollecitom.libs.swissknife.pagination.domain.order

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class SortOrderTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating an ascending sort order`() {

            val order = SortOrder(SortOrderDirection.ASCENDING)

            assertThat(order.direction).isEqualTo(SortOrderDirection.ASCENDING)
        }

        @Test
        fun `creating a descending sort order`() {

            val order = SortOrder(SortOrderDirection.DESCENDING)

            assertThat(order.direction).isEqualTo(SortOrderDirection.DESCENDING)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class SortOrderDirectionValues {

        @Test
        fun `sort order direction has two values`() {

            val values = SortOrderDirection.entries

            assertThat(values.size).isEqualTo(2)
        }

        @Test
        fun `sort order directions are ascending and descending`() {

            assertThat(SortOrderDirection.valueOf("ASCENDING")).isEqualTo(SortOrderDirection.ASCENDING)
            assertThat(SortOrderDirection.valueOf("DESCENDING")).isEqualTo(SortOrderDirection.DESCENDING)
        }
    }
}
