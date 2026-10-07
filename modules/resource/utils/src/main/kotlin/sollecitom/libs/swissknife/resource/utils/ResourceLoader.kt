package sollecitom.libs.swissknife.resource.utils

import com.google.common.io.Resources
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.toPath

/** Loads resources by URL or classpath name. Resources packaged inside a jar have no file-system [Path]: read them with [openAsStream] or [readAsText]. */
object ResourceLoader {

    /** Only for resources on the file system; fails for resources inside a jar. */
    fun resolveAbsolutePath(resourceName: String): String = resolvePath(resourceName).absolutePathString()

    fun openAsStream(resourceName: String): InputStream = resolve(resourceName).openStream()

    /** Only for resources on the file system; fails for resources inside a jar. */
    fun resolvePath(resourceName: String): Path = resolve(resourceName).let { url ->
        require(url.protocol == "file") { "Resource $resourceName is not on the file system ($url), so it has no Path: use openAsStream or readAsText instead." }
        url.toURI().toPath()
    }

    fun readAsText(resourceName: String): String = resolve(resourceName).readText()

    fun resolve(resourceName: String): URL = try {
        URI.create(resourceName).toURL()
    } catch (error: IllegalArgumentException) {
        Resources.getResource(resourceName)
    } catch (error: MalformedURLException) {
        Resources.getResource(resourceName)
    }
}
