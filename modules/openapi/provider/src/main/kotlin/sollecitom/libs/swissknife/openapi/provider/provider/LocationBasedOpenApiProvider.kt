package sollecitom.libs.swissknife.openapi.provider.provider

import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.AuthorizationValue
import io.swagger.v3.parser.exception.ReadContentException
import io.swagger.v3.parser.util.ClasspathHelper
import io.swagger.v3.parser.util.RemoteUrl
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.net.ssl.SSLHandshakeException

class LocationBasedOpenApiProvider(private val openApiFileName: String) : OpenApiProvider {

    private val encoding = StandardCharsets.UTF_8.displayName()
    override val openApiLocation: String get() = openApiFileName
    override val openApi: OpenAPI get() = OpenApiReader.parse(openApiFileName)
    override val rawOpenApi: String by lazy { readContentFromLocation(openApiLocation, emptyList()) }

    private fun readContentFromLocation(location: String, auth: List<AuthorizationValue>): String {

        val adjustedLocation = location.replace("\\\\".toRegex(), "/")
        try {
            if (adjustedLocation.lowercase(Locale.getDefault()).startsWith("http")) {
                return RemoteUrl.urlToString(adjustedLocation, auth)
            } else if (adjustedLocation.lowercase(Locale.getDefault()).startsWith("jar:")) {
                val inputStream = URI(adjustedLocation).toURL().openStream()
                return IOUtils.toString(inputStream, encoding)
            } else {
                val fileScheme = "file:"
                val path = if (adjustedLocation.lowercase(Locale.getDefault()).startsWith(fileScheme)) Paths.get(URI.create(adjustedLocation)) else Paths.get(adjustedLocation)
                return if (Files.exists(path)) {
                    FileUtils.readFileToString(path.toFile(), encoding)
                } else {
                    ClasspathHelper.loadFileFromClasspath(adjustedLocation)
                }
            }
        } catch (e: SSLHandshakeException) {
            val message = String.format(
                "Unable to read location `%s` due to a SSL configuration error. It is possible that the server SSL certificate is invalid, self-signed, or has an untrusted Certificate Authority.",
                adjustedLocation
            )
            throw ReadContentException(message, e)
        } catch (e: Exception) {
            throw ReadContentException(String.format("Unable to read location `%s`", adjustedLocation), e)
        }
    }
}