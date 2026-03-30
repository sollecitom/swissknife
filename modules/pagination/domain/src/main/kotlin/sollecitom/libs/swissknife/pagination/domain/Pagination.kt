package sollecitom.libs.swissknife.pagination.domain

import sollecitom.libs.swissknife.core.domain.text.Name

/** Token-based pagination primitives. */
data object Pagination {

    /** Input arguments for a paginated query: page size and optional continuation token. */
    data class Arguments(val limit: Int, val continuationToken: Name? = null) {

        companion object
    }

    /** Pagination metadata returned with a page: total count and optional continuation token for the next page. */
    data class Information(val totalItemsCount: Long, val continuationToken: Name? = null) {

        val hasMoreElements: Boolean get() = continuationToken != null

        companion object
    }
}