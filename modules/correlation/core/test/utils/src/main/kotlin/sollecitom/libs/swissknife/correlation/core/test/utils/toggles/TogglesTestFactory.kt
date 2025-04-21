package sollecitom.libs.swissknife.correlation.core.test.utils.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.*

fun Toggles.Companion.create(values: Set<ToggleValue<*>> = emptySet()) = Toggles(values)

context(ids: UniqueIdGenerator)
fun ToggleValue.Companion.boolean(value: Boolean, id: Id = ids.newId()) = BooleanToggleValue(id, value)

context(ids: UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Int, id: Id = ids.newId()) = integer(value.toLong(), id)

context(ids: UniqueIdGenerator)
fun ToggleValue.Companion.integer(value: Long, id: Id = ids.newId()) = IntegerToggleValue(id, value)

context(ids: UniqueIdGenerator)
fun ToggleValue.Companion.decimal(value: Double, id: Id = ids.newId()) = DecimalToggleValue(id, value)

context(ids: UniqueIdGenerator)
fun <VALUE : Enum<VALUE>> ToggleValue.Companion.enum(value: VALUE, id: Id = ids.newId()) = EnumToggleValue(id, value.name)