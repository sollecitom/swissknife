package sollecitom.libs.swissknife.configuration.utils

/** Base class for defining a group of configuration keys under a common [rootPath] prefix. */
abstract class ConfigurationSection(private val rootPath: String? = null) {

    private val sectionPrefix = rootPath?.let { "$it." } ?: ""

    protected fun fullPath(key: String) = "${sectionPrefix}$key"
}