package sollecitom.libs.swissknife.configuration.utils

import org.http4k.config.Environment
import java.io.File

/**
 * Builds a standard cascading [Environment] with the following precedence (highest to lowest):
 * external config files > JVM system properties > OS environment variables > [defaultConfiguration].
 */
object StandardEnvironment {

    operator fun invoke(additionalExternalConfigFiles: List<File> = emptyList(), defaultConfiguration: Environment = Environment.EMPTY): Environment {

        val standardCascadingEnvironment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides defaultConfiguration
        return Environment.fromFiles(additionalExternalConfigFiles) overrides standardCascadingEnvironment
    }
}