package sollecitom.libs.swissknife.lens.core.extensions.utils

import org.http4k.config.Environment
import org.http4k.lens.BiDiLens
import java.util.*

/** Converts a lens's dotted meta name (e.g., "service.port") to an environment variable name (e.g., "SERVICE_PORT"). */
val BiDiLens<Environment, *>.environmentName: String get() = meta.name.toEnvironmentName()

private fun String.toEnvironmentName(): String = split(".").joinToString(separator = "_") { it.uppercase(Locale.UK) }