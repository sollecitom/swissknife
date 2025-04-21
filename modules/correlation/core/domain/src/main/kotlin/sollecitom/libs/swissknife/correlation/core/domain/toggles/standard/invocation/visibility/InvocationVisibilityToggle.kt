package sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility

import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggle
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.template.EnumToggle

private object InvocationVisibilityToggle : Toggle<InvocationVisibility, String> by EnumToggle(id = "invocation-visibility".let(::StringId), deserializeValue = InvocationVisibility::valueOf)

val Toggles.Companion.InvocationVisibility: Toggle<InvocationVisibility, String> get() = InvocationVisibilityToggle