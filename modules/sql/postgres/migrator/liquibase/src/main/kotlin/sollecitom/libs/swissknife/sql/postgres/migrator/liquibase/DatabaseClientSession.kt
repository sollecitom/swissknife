package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

internal data class DatabaseClientSession(val pid: String, val startTime: String) {

    val lockedByValue get() = "$pid${Constants.Values.LOCKED_BY_SEPARATOR}$startTime"

    // TODO make startTime an Instant, by startTime.toLong().let { Instant.fromEpochMillis(it) }
    override fun toString() = "(pid=${pid} , startTime=${startTime})"
}