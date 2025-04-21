package sollecitom.libs.swissknife.http4k.utils

object HttpHeaders {

    val ContentType: HttpHeader get() = ContentTypeHeader
    val ContentLength: HttpHeader get() = ContentLengthHeader
    val Location: HttpHeader get() = LocationHeader
    val UserAgent: HttpHeader get() = LocationHeader
    val Authorization: HttpHeader get() = AuthorizationHeader
}

private object ContentTypeHeader : HttpHeader {

    override val name = "content-type"
}

private object AuthorizationHeader : HttpHeader {

    override val name = "authorization"
}

private object UserAgent : HttpHeader {

    override val name = "user-agent"
}

private object ContentLengthHeader : HttpHeader {

    override val name = "content-length"
}

private object LocationHeader : HttpHeader {

    override val name = "location"
}