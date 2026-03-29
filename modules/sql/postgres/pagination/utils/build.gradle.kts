plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.sqlPostgresUtils)
    api(projects.paginationDomain)
}