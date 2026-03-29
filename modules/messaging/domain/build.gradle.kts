plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.dddDomain)
    api(projects.readinessDomain)

    implementation(projects.dddLoggingUtils)

    testImplementation(projects.dddTestUtils)
    testImplementation(projects.messagingTestUtils)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}