package sollecitom.libs.swissknife.http4k.utils.lens

import org.http4k.lens.Header

val Header.USER_AGENT get() = optional("user-agent")