plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.sqlMigratorLiquibase)

    implementation(projects.loggerCore)
}