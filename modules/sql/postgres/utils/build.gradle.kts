plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.sqlReactiveUtils)
    api(libs.r2dbc.postgres)
    api(projects.readinessDomain)

    implementation(projects.loggerCore)
}