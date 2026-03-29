plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.protectedValueDomain)
    api(projects.cryptographyDomain)

    implementation(projects.cryptographyImplementationBouncyCastle)

    testImplementation(projects.correlationCoreTestUtils)
    testImplementation(projects.testUtils)
}