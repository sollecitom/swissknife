package sollecitom.libs.swissknife.pagination.domain

/** A single page of results with associated pagination [information]. */
data class Page<out ITEM>(val items: List<ITEM>, val information: Pagination.Information)