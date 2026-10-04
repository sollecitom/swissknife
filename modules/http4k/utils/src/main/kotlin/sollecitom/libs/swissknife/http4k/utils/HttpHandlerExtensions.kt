package sollecitom.libs.swissknife.http4k.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import org.http4k.client.AsyncHttpHandler
import org.http4k.core.Request
import org.http4k.core.Response

/** Suspending extension to invoke an [AsyncHttpHandler], bridging the callback-based API to coroutines. */
suspend operator fun AsyncHttpHandler.invoke(request: Request): Response = suspendCancellableCoroutine { continuation ->

    invoke(request) { response ->
        continuation.resume(response) { _, unclaimedResponse, _ -> unclaimedResponse.close() }
    }
}