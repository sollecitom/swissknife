package sollecitom.libs.swissknife.correlation.core.test.utils.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.*

fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)

context(UniqueIdGenerator)
fun ToggleValue.Companion.boolean(value: Boolean, id: Id = newId()) = BooleanToggleValue(id, value)

context(UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Int, id: Id = newId()) = integer(value.toLong(), id)

context(UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Long, id: Id = newId()) = IntegerToggleValue(id, value)

context(UniqueIdGenerator)
fun ToggleValue.Companion.decimal(value: Double, id: Id = newId()) = DecimalToggleValue(id, value)

context(UniqueIdGenerator)
fun <VALUE : Enum<VALUE>> ToggleValue.Companion.enum(value: VALUE, id: Id = newId()) = EnumToggleValue(id, value.name)