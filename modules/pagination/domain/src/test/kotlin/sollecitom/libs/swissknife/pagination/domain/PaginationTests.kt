package sollecitom.libs.swissknife.pagination.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.core.domain.text.Name
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class PaginationTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Arguments {

        @Test
        fun `creating pagination arguments with limit only`() {

            val args = Pagination.Arguments(limit = 10)

            assertThat(args.limit).isEqualTo(10)
            assertThat(args.continuationToken).isNull()
        }

        @Test
        fun `creating pagination arguments with limit and continuation token`() {

            val token = Name("next-page-token")
            val args = Pagination.Arguments(limit = 20, continuationToken = token)

            assertThat(args.limit).isEqualTo(20)
            assertThat(args.continuationToken).isEqualTo(token)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Information {

        @Test
        fun `information with no continuation token indicates no more elements`() {

            val info = Pagination.Information(totalItemsCount = 5)

            assertThat(info.hasMoreElements).isFalse()
            assertThat(info.continuationToken).isNull()
        }

        @Test
        fun `information with a continuation token indicates more elements`() {

            val token = Name("next-token")
            val info = Pagination.Information(totalItemsCount = 100, continuationToken = token)

            assertThat(info.hasMoreElements).isTrue()
            assertThat(info.continuationToken).isEqualTo(token)
        }

        @Test
        fun `total items count is stored correctly`() {

            val info = Pagination.Information(totalItemsCount = 42)

            assertThat(info.totalItemsCount).isEqualTo(42L)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class PageCreation {

        @Test
        fun `creating a page with items`() {

            val items = listOf("a", "b", "c")
            val info = Pagination.Information(totalItemsCount = 10, continuationToken = Name("next"))
            val page = Page(items, info)

            assertThat(page.items).isEqualTo(items)
            assertThat(page.information).isEqualTo(info)
        }

        @Test
        fun `creating an empty page`() {

            val items = emptyList<String>()
            val info = Pagination.Information(totalItemsCount = 0)
            val page = Page(items, info)

            assertThat(page.items).isEqualTo(emptyList())
            assertThat(page.information.totalItemsCount).isEqualTo(0L)
            assertThat(page.information.hasMoreElements).isFalse()
        }
    }
}
