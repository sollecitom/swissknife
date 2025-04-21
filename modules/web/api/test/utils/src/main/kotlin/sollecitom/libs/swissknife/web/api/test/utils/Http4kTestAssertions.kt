package sollecitom.libs.swissknife.web.api.test.utils

import assertk.Assert
import assertk.assertions.isTrue
import org.http4k.core.ContentType

fun Assert<ContentType>.isEqualToIgnoringDirectives(other: ContentType) = given { contentType ->

    assertThat(contentType.equalsIgnoringDirectives(other)).isTrue()
}