package sollecitom.libs.swissknife.correlation.core.domain.utils

import sollecitom.libs.swissknife.core.domain.locale.WithDefaultLocale
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.localeOrNull
import java.util.*

context(InvocationContext<*>, WithDefaultLocale)
val locale: Locale
    get() = localeOrNull ?: defaultLocale