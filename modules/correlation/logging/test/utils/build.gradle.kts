plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.correlationLoggingUtils)
    api(projects.correlationCoreTestUtils)

    testImplementation(projects.coreTestUtils)
    testRuntimeOnly(projects.loggerSlf4jAdapter)
}
