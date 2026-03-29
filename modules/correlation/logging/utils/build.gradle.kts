plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.correlationCoreDomain)
    api(projects.loggerCore)

    implementation(projects.kotlinExtensions)
}