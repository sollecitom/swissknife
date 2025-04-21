package sollecitom.libs.swissknife.pagination.domain

data class Page<out ITEM>(val items: List<ITEM>, val information: Pagination.Information)