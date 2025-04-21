package sollecitom.libs.swissknife.jwt.domain

import java.net.URI

data class StringOrURI(val value: String) {

    constructor(uri: URI) : this(uri.toString())

    companion object
}