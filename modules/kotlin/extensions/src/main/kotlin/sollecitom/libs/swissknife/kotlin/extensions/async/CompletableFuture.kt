package sollecitom.libs.swissknife.kotlin.extensions.async

import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

suspend fun CompletableFuture<Void>.await(): Unit = await().let { }