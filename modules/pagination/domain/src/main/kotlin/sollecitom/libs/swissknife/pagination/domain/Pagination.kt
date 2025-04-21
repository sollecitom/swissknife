package sollecitom.libs.swissknife.pagination.domain

import sollecitom.libs.swissknife.core.domain.text.Name

data object Pagination {

    data class Arguments(val limit: Int, val continuationToken: Name? = null) {

        companion object
    }

    data class Information(val totalItemsCount: Long, val continuationToken: Name? = null) {

        val hasMoreElements: Boolean get() = continuationToken != null

        companion object
    }
}