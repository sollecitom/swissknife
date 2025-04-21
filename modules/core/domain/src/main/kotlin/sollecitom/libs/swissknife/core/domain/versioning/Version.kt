package sollecitom.libs.swissknife.core.domain.versioning

interface Version<SELF : Version<SELF>> : Comparable<SELF>