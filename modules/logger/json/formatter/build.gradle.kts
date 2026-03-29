plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.loggerCore)
    implementation(projects.jsonUtils)
    implementation(projects.resourceUtils)

    testImplementation(projects.testUtils)
    testImplementation(projects.jsonTestUtils)
}