plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.jsonUtils)
    api(projects.pulsarUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}