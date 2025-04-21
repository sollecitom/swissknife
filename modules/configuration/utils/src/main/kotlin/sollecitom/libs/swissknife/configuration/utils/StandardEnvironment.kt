package sollecitom.libs.swissknife.configuration.utils

import org.http4k.config.Environment
import java.io.File

object StandardEnvironment {

    operator fun invoke(additionalExternalConfigFiles: List<File> = emptyList(), defaultConfiguration: Environment = Environment.EMPTY): Environment {

        val standardCascadingEnvironment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides defaultConfiguration
        return Environment.fromFiles(additionalExternalConfigFiles) overrides standardCascadingEnvironment
    }
}