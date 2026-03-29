plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.dddDomain)
    api(projects.coreUtils)
    api(projects.coreTestUtils)
    api(projects.correlationCoreTestUtils)
    api(projects.testUtils)
}