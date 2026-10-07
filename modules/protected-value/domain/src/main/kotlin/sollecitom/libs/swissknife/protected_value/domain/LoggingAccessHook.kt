package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlin.time.Clock

fun <ACCESS_CONTEXT : Any> ProtectedValue.Companion.loggingAccessHook(clock: Clock = Clock.System): ProtectedValue.AccessHook<ACCESS_CONTEXT, Any?> = ProtectedValue.AccessHook { context, value ->
    LoggingAccessHook.logger.info { "Protected value '${value.name.value}' of owner '${value.owner.stringValue}' accessed at ${clock.now()} by $context" }
}

private object LoggingAccessHook : Loggable()
