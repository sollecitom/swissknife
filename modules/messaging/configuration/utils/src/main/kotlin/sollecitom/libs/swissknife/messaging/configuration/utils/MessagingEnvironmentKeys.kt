package sollecitom.libs.swissknife.messaging.configuration.utils

import sollecitom.libs.swissknife.messaging.domain.topic.TenantAgnosticTopic
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map

fun <IN : Any> BiDiLensSpec<IN, String>.topic() = map(StringBiDiMappings.topic())

fun StringBiDiMappings.topic() = BiDiMapping(Topic::parse) { it.fullName.value }

fun <IN : Any> BiDiLensSpec<IN, String>.tenantAgnosticTopic() = map(StringBiDiMappings.tenantAgnosticTopic())

fun StringBiDiMappings.tenantAgnosticTopic() = BiDiMapping(TenantAgnosticTopic.Companion::parse) { it.name.value }