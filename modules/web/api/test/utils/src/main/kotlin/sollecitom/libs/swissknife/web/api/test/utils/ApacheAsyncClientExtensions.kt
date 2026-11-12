package sollecitom.libs.swissknife.web.api.test.utils

import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.cookie.StandardCookieSpec
import org.apache.hc.client5.http.impl.async.HttpAsyncClients
import org.http4k.client.ApacheAsyncClient
import org.http4k.client.AsyncHttpHandler
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.time.Duration

fun ApacheAsyncClient.withResponseTimeout(responseTimeout: Duration): AsyncHttpHandler {

    val requestConfig = RequestConfig.custom().setRedirectsEnabled(false).setCookieSpec(StandardCookieSpec.IGNORE).setResponseTimeout(responseTimeout.inWholeMilliseconds, MILLISECONDS).build()
    return this(HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build().apply { start() })
}
