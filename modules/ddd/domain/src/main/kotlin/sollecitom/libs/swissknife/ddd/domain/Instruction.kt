package sollecitom.libs.swissknife.ddd.domain

/** An inbound request to the system, either a [Command] or a [Query]. */
sealed interface Instruction : Happening {

    companion object
}