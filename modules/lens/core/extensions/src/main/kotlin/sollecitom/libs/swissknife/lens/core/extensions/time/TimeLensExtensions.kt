package sollecitom.libs.swissknife.lens.core.extensions.time

import org.http4k.config.Environment
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.duration
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration
import java.time.Duration as JavaDuration

/** Creates a lens for Kotlin [Duration] values, converting through Java Duration. */
fun BiDiLensSpec<Environment, String>.kotlinDuration() = duration().map(JavaDuration::toKotlinDuration, Duration::toJavaDuration)