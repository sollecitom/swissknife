package sollecitom.libs.swissknife.core.domain.versioning

interface Versioned<VERSION : Comparable<VERSION>> {

    val version: VERSION
}