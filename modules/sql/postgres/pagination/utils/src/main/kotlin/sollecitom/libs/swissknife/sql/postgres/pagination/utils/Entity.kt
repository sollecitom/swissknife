
package sollecitom.libs.swissknife.sql.postgres.pagination.utils

data class Entity<out VALUE : Any>(val primaryKey: Long, val value: VALUE)