package sollecitom.libs.swissknife.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

interface Startable {

    suspend fun start() {}
}

fun Startable.startBlocking() {

    runBlocking { start() }
}