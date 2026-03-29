plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)
    api(projects.correlationCoreDomain)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
    testImplementation(projects.correlationCoreTestUtils)
}