package sollecitom.libs.swissknife.correlation.core.domain.utils

import sollecitom.libs.swissknife.core.domain.locale.WithDefaultLocale
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.localeOrNull
import java.util.*

context(context: InvocationContext<*>, withLocale: WithDefaultLocale)
val locale: Locale
    get() = context.localeOrNull ?: withLocale.defaultLocale