plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.natsClient)
    api(projects.testContainersUtils)
    api(projects.coreTestUtils)
}