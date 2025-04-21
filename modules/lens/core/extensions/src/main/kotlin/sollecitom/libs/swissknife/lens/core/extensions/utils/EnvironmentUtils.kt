package sollecitom.libs.swissknife.lens.core.extensions.utils

import org.http4k.config.Environment
import org.http4k.lens.BiDiLens
import java.util.*

val BiDiLens<Environment, *>.environmentName: String get() = meta.name.toEnvironmentName()

private fun String.toEnvironmentName(): String = split(".").joinToString(separator = "_") { it.uppercase(Locale.UK) }