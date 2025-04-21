package sollecitom.libs.swissknife.http4k.utils

import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.json.utils.serde.JsonSerializer
import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.lens.Header

val Request.contentType: ContentType? get() = Header.CONTENT_TYPE(this)

fun <VALUE : Any> Request.body(value: VALUE, serializer: JsonSerializer<VALUE>) = body(serializer.serialize(value))

fun Request.header(header: HttpHeader, value: String) = header(header.name, value)
fun Request.replaceHeader(header: HttpHeader, value: String) = replaceHeader(header.name, value)

fun Request.contentType(contentType: ContentType) = replaceHeader(HttpHeaders.ContentType, contentType.toHeaderValue())

fun Request.contentLength(contentLength: Int) = contentLength(contentLength.toLong())
fun Request.contentLength(contentLength: Long) = replaceHeader(HttpHeaders.ContentLength, contentLength.toString())

val Request.ipAddress: IpAddress? get() = source?.address?.let(IpAddress::create)

fun Request.bearerToken(token: String) = header(HttpHeaders.Authorization, "Bearer $token")