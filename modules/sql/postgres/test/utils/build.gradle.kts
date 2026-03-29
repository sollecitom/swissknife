plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.sqlPostgresContainer)
    api(projects.testUtils)
    api(projects.sqlMigratorDomain)
    api(projects.sqlMigratorLiquibase)

    runtimeOnly(libs.postgres)
}