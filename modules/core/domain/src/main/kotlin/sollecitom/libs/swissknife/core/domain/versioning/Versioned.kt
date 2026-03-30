package sollecitom.libs.swissknife.core.domain.versioning

/** A type that carries a version number. */
interface Versioned<VERSION : Comparable<VERSION>> {

    val version: VERSION
}