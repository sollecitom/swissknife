plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.dddDomain)
    api(projects.loggerCore)
    api(projects.kotlinExtensions)
    api(projects.correlationLoggingUtils)

    testImplementation(projects.testUtils)
}