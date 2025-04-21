package sollecitom.libs.swissknife.sql.postgres.migrator.liquibase

internal object Constants {

    object ServicePriority {
        const val MAX = Int.MAX_VALUE
    }

    object Databases {
        private val postgres = "PostgreSQL"
        val supportedDatabaseNames = setOf(postgres)
    }

    object Fields {
        const val LOCKED = "LOCKED"
        const val LOCKED_BY = "LOCKEDBY"
        const val LOCKED_GRANTED = "LOCKGRANTED"
        const val PID = "PID"
        const val BACKEND_START = "BACKEND_START"
        const val ID = "ID"
    }

    object Values {
        const val LOCKED_BY_SEPARATOR = "@@"
    }

    object Tables {
        const val PG_STAT_ACTIVITY = "pg_stat_activity"
    }
}