plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    testImplementation(projects.openapiCheckingTestUtils)
    testImplementation(projects.kotlinExtensions)
    testImplementation(projects.resourceUtils)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}