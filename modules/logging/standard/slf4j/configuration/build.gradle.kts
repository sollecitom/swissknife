plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.loggingStandardConfiguration)
    runtimeOnly(projects.loggerSlf4jAdapter)

    testImplementation(projects.testUtils)
    testImplementation(projects.jsonTestUtils)
}