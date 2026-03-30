package sollecitom.libs.swissknife.jwt.domain

import java.net.URI

/** A value that is either a plain string or a URI string, as defined in the JWT specification (RFC 7519). */
data class StringOrURI(val value: String) {

    constructor(uri: URI) : this(uri.toString())

    companion object
}